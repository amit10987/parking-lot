package com.assessment.parkinglot.domain.entity;

import com.assessment.parkinglot.domain.valueobject.Role;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "owner")
    private List<Vehicle> vehicles = new ArrayList<>();

    // Mandatory constructor
    public User(String email, Role role) {
        if (email == null || email.isBlank() || role == null) {
            throw new IllegalArgumentException("Email and role are required");
        }
        this.email = email;
        this.role = role;
    }

    protected User() {} // JPA default

    // Optional setters
    public void addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) {
        this.vehicles.remove(vehicle);
    }
}

