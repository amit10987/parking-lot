package com.assessment.parkinglot.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class EntryGate {
    @Id
    @GeneratedValue
    private Long id;

    private int floor;
    private boolean active = true;

    @ManyToOne(optional = false)
    private ParkingLot parkingLot;

    protected EntryGate() {}

    public EntryGate(ParkingLot lot, int floor) {
        this.parkingLot = lot;
        this.floor = floor;
    }

    public Long getId() { return id; }
    public int getFloor() { return floor; }
    public boolean isActive() { return active; }

    public void deactivate() {
        this.active = false;
    }
}

