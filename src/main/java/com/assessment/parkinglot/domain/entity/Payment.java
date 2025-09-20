package com.assessment.parkinglot.domain.entity;

import com.assessment.parkinglot.domain.valueobject.PaymentStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id @GeneratedValue
    private Long id;

    @OneToOne(optional = false)
    private Ticket ticket;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime timestamp;

    // Mandatory constructor
    public Payment(Ticket ticket, BigDecimal amount, PaymentStatus status) {
        if (ticket == null || amount == null || status == null) {
            throw new IllegalArgumentException("Ticket, amount, and status are required");
        }
        this.ticket = ticket;
        this.amount = amount;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    protected Payment() {}

    // Business method
    public void markSuccess() {
        this.status = PaymentStatus.SUCCESS;
        this.timestamp = LocalDateTime.now();
    }

    public void markFailed() {
        this.status = PaymentStatus.FAILED;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
