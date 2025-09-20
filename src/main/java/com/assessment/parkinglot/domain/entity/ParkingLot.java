package com.assessment.parkinglot.domain.entity;

import com.assessment.parkinglot.domain.valueobject.SlotStatus;
import com.assessment.parkinglot.domain.valueobject.VehicleType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parking_lots")
public class ParkingLot {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String location;

    private int floors;

    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL)
    private List<ParkingSlot> slots = new ArrayList<>();

    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL)
    private List<EntryGate> gates = new ArrayList<>();

    // Mandatory constructor
    public ParkingLot(String location, int floors) {
        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException("Location is required");
        }
        this.location = location;
        this.floors = floors;
    }

    protected ParkingLot() {}

    // Business methods
    public ParkingSlot addSlot(VehicleType type, int floor) {
        ParkingSlot slot = new ParkingSlot(this, type, floor);
        this.slots.add(slot);
        return slot;
    }

    public void removeSlot(ParkingSlot slot) {
        this.slots.remove(slot);
        slot.deactivate();
    }

    public int getTotalSlots() {
        return slots.size();
    }

    public int getAvailableSlots() {
        return (int) slots.stream().filter(s -> s.getStatus() == SlotStatus.AVAILABLE).count();
    }

    public Long getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public int getFloors() {
        return floors;
    }

    public List<ParkingSlot> getSlots() {
        return slots;
    }

    public EntryGate addGate(int floor) {
        EntryGate gate = new EntryGate(this, floor);
        this.gates.add(gate);
        return gate;
    }

    public List<EntryGate> getGates() {
        return gates;
    }
}
