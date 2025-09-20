package com.assessment.parkinglot.application;

import com.assessment.parkinglot.application.slotstrategy.SlotAllocationStrategy;
import com.assessment.parkinglot.domain.entity.*;
import com.assessment.parkinglot.domain.valueobject.PaymentStatus;
import com.assessment.parkinglot.domain.valueobject.Receipt;
import com.assessment.parkinglot.domain.valueobject.SlotStatus;
import com.assessment.parkinglot.domain.valueobject.TicketStatus;
import com.assessment.parkinglot.infra.repository.EntryGateRepository;
import com.assessment.parkinglot.infra.repository.ParkingSlotRepository;
import com.assessment.parkinglot.infra.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ParkingService {

    private final ParkingSlotRepository slotRepository;
    private final TicketRepository ticketRepository;
    private final EntryGateRepository gateRepository;
    private final PaymentService paymentService;
    private final FeeService feeService;

    private final Map<String, SlotAllocationStrategy> strategies;

    public ParkingService(ParkingSlotRepository slotRepository,
                          TicketRepository ticketRepository,
                          EntryGateRepository gateRepository,
                          PaymentService paymentService,
                          FeeService feeService,
                          @Qualifier("nearest") SlotAllocationStrategy nearestStrategy,
                          @Qualifier("firstAvailable") SlotAllocationStrategy firstAvailableStrategy,
                          @Qualifier("random") SlotAllocationStrategy randomStrategy,
                          @Qualifier("levelWise") SlotAllocationStrategy levelWiseStrategy) {
        this.slotRepository = slotRepository;
        this.ticketRepository = ticketRepository;
        this.gateRepository = gateRepository;
        this.paymentService = paymentService;
        this.feeService = feeService;

        this.strategies = Map.of(
                "nearest", nearestStrategy,
                "first", firstAvailableStrategy,
                "random", randomStrategy,
                "levelwise", levelWiseStrategy
        );
    }

    @Transactional
    public Ticket parkVehicle(Vehicle vehicle, Long gateId, String strategyType) {
        // Check if vehicle is already parked
        boolean alreadyParked = ticketRepository.existsByVehicleAndStatus(vehicle, TicketStatus.ACTIVE);
        if (alreadyParked) {
            throw new IllegalStateException("Vehicle is already parked");
        }
        // Validate entry gate
        EntryGate gate = gateRepository.findById(gateId)
                .orElseThrow(() -> new RuntimeException("Invalid gate"));

        // Allocate nearest available slot
        SlotAllocationStrategy strategy = strategies.getOrDefault(strategyType, strategies.get("nearest"));
        ParkingSlot slot = strategy.findNearestSlot(vehicle.getType(), gate);

        // Mark slot as occupied
        slot.setStatus(SlotStatus.OCCUPIED);
        slotRepository.save(slot);

        // Create ticket using entity constructor
        Ticket ticket = new Ticket(vehicle, slot, gate);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Receipt exitVehicle(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Invalid ticket"));

        if (ticket.getStatus() != TicketStatus.ACTIVE) {
            throw new RuntimeException("Ticket already closed");
        }

        // Calculate parking fees using FeeService
        BigDecimal amount = feeService.calculateFee(ticket.getEntryTime(), LocalDateTime.now(), ticket.getSlot().getType());

        // Process payment
        Payment payment = paymentService.processPayment(ticket, amount);
        if (!payment.getStatus().equals(PaymentStatus.SUCCESS)) {
            throw new RuntimeException("Payment failed, cannot free slot");
        }

        // Free slot and close ticket
        ParkingSlot slot = ticket.getSlot();
        slot.setStatus(SlotStatus.AVAILABLE);
        slotRepository.save(slot);

        ticket.markExited();
        ticketRepository.save(ticket);

        return new Receipt(ticket.getVehicle().getPlateNo(), slot.getId(), amount, ticket.getEntryTime(), ticket.getExitTime());
    }

}
