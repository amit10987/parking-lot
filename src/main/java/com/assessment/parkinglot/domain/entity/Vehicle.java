package com.assessment.parkinglot.domain.entity;

import com.assessment.parkinglot.domain.valueobject.VehicleType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles", uniqueConstraints = {@UniqueConstraint(columnNames = "plateNo")})
public class Vehicle {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String plateNo;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    @ManyToOne(optional = false)
    private User owner;

    @OneToMany(mappedBy = "vehicle")
    private List<Ticket> tickets = new ArrayList<>();

    // Mandatory constructor
    public Vehicle(User owner, String plateNo, VehicleType type) {
        if (owner == null || plateNo == null || type == null) {
            throw new IllegalArgumentException("Owner, plate number, and type are mandatory");
        }
        this.owner = owner;
        this.plateNo = plateNo;
        this.type = type;
    }

    protected Vehicle() {} // JPA default

    // Optional setters
    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
    }

    public VehicleType getType() {
        return type;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public User getOwner() {
        return owner;
    }
}
