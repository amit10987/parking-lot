package com.assessment.parkinglot.application;

import com.assessment.parkinglot.domain.entity.*;
import com.assessment.parkinglot.domain.valueobject.*;
import com.assessment.parkinglot.infra.repository.EntryGateRepository;
import com.assessment.parkinglot.infra.repository.ParkingSlotRepository;
import com.assessment.parkinglot.infra.repository.TicketRepository;
import com.assessment.parkinglot.infra.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ParkingService {

    private final ParkingSlotRepository slotRepository;
    private final TicketRepository ticketRepository;
    private final EntryGateRepository gateRepository;
    private final SlotAllocationStrategy slotStrategy;
    private final PaymentService paymentService;
    private final VehicleRepository vehicleRepository;
    private final FeeService feeService;

    public ParkingService(ParkingSlotRepository slotRepository, TicketRepository ticketRepository, EntryGateRepository gateRepository, SlotAllocationStrategy slotStrategy, PaymentService paymentService, VehicleRepository vehicleRepository, FeeService feeService) {
        this.slotRepository = slotRepository;
        this.ticketRepository = ticketRepository;
        this.gateRepository = gateRepository;
        this.slotStrategy = slotStrategy;
        this.paymentService = paymentService;
        this.vehicleRepository = vehicleRepository;
        this.feeService = feeService;
    }

    public Ticket parkVehicle(Vehicle vehicle, Long gateId) {
        // Validate entry gate
        EntryGate gate = gateRepository.findById(gateId)
                .orElseThrow(() -> new RuntimeException("Invalid gate"));

        // Allocate nearest available slot
        ParkingSlot slot = slotStrategy.findNearestSlot(vehicle.getType(), gate);

        // Mark slot as occupied
        slot.setStatus(SlotStatus.OCCUPIED);
        slotRepository.save(slot);

        // Create ticket using entity constructor
        Ticket ticket = new Ticket(vehicle, slot, gate);

        return ticketRepository.save(ticket);
    }


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
