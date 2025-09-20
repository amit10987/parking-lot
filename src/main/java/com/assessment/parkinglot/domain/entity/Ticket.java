package com.assessment.parkinglot.domain.entity;

import com.assessment.parkinglot.domain.valueobject.TicketStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private Vehicle vehicle;

    @ManyToOne(optional = false)
    private ParkingSlot slot;

    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.ACTIVE;

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private Payment payment;

    // Optional: reference to entry gate
    @ManyToOne
    private EntryGate gate;

    // Constructor for mandatory fields
    public Ticket(Vehicle vehicle, ParkingSlot slot, EntryGate gate) {
        if (vehicle == null || slot == null) {
            throw new IllegalArgumentException("Vehicle and slot are mandatory");
        }
        this.vehicle = vehicle;
        this.slot = slot;
        this.gate = gate;
        this.entryTime = LocalDateTime.now();
        this.status = TicketStatus.ACTIVE;
    }

    // Optional setters for exitTime, status, etc.
    public void markExited() {
        this.exitTime = LocalDateTime.now();
        this.status = TicketStatus.CLOSED;
    }

    protected Ticket() {}

    // Business methods
    public void closeTicket() {
        this.exitTime = LocalDateTime.now();
        this.status = TicketStatus.CLOSED;
    }

    public void assignPayment(Payment payment) {
        this.payment = payment;
    }

    public ParkingSlot getSlot() {
        return slot;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }
}
