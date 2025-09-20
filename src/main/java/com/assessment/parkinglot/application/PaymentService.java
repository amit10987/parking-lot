package com.assessment.parkinglot.application;

import com.assessment.parkinglot.domain.entity.Payment;
import com.assessment.parkinglot.domain.entity.Ticket;
import com.assessment.parkinglot.domain.valueobject.PaymentStatus;
import com.assessment.parkinglot.domain.valueobject.SlotStatus;
import com.assessment.parkinglot.domain.valueobject.TicketStatus;
import com.assessment.parkinglot.infra.repository.PaymentRepository;
import com.assessment.parkinglot.infra.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;

    public PaymentService(PaymentRepository paymentRepository, TicketRepository ticketRepository) {
        this.paymentRepository = paymentRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public Payment processPayment(Ticket ticket, BigDecimal amount) {
        if (ticket.getStatus() != TicketStatus.ACTIVE) {
            throw new IllegalStateException("Ticket is already closed");
        }

        // Create Payment entity
        Payment payment = new Payment(ticket, amount, PaymentStatus.SUCCESS);
        ticket.assignPayment(payment);

        // Close ticket and free slot
        ticket.closeTicket();
        ticket.getSlot().setStatus(SlotStatus.AVAILABLE);

        paymentRepository.save(payment);
        ticketRepository.save(ticket);

        return payment;
    }
}
