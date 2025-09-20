package com.assessment.parkinglot.domain.entity;

import com.assessment.parkinglot.domain.valueobject.SlotStatus;
import com.assessment.parkinglot.domain.valueobject.VehicleType;
import jakarta.persistence.*;

@Entity
@Table(name = "parking_slots")
public class ParkingSlot {

    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType type;

    private int floor;

    @Enumerated(EnumType.STRING)
    private SlotStatus status = SlotStatus.AVAILABLE;

    @ManyToOne(optional = false)
    private ParkingLot parkingLot;

    @OneToOne(mappedBy = "slot")
    private Ticket ticket;

    private int distanceFromGate;

    // Mandatory constructor
    public ParkingSlot(ParkingLot parkingLot, VehicleType type, int floor) {
        if (parkingLot == null || type == null) {
            throw new IllegalArgumentException("ParkingLot and VehicleType are required");
        }
        this.parkingLot = parkingLot;
        this.type = type;
        this.floor = floor;
    }

    protected ParkingSlot() {}

    // Optional setters
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void setStatus(SlotStatus status) {
        this.status = status;
    }

    // Business methods
    public void deactivate() {
        this.status = SlotStatus.AVAILABLE; // or INACTIVE if added
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public VehicleType getType() {
        return type;
    }

    public int getFloor() {
        return floor;
    }

    public SlotStatus getStatus() {
        return status;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public Long getId() {
        return id;
    }
}
