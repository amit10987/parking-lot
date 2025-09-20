package com.assessment.parkinglot.application;

import com.assessment.parkinglot.domain.entity.Ticket;
import com.assessment.parkinglot.domain.entity.User;
import com.assessment.parkinglot.domain.entity.Vehicle;
import com.assessment.parkinglot.domain.valueobject.TicketStatus;
import com.assessment.parkinglot.domain.valueobject.VehicleType;
import com.assessment.parkinglot.infra.repository.TicketRepository;
import com.assessment.parkinglot.infra.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final TicketRepository ticketRepository;

    public VehicleService(VehicleRepository vehicleRepository, TicketRepository ticketRepository) {
        this.vehicleRepository = vehicleRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public Vehicle registerVehicle(User owner, String plateNo, VehicleType type) {
        return vehicleRepository.findByPlateNo(plateNo)
                .map(vehicle -> {
                    // If vehicle exists but with different owner, handle edge case
                    if (!vehicle.getOwner().equals(owner)) {
                        throw new IllegalStateException(
                                "Vehicle with plate " + plateNo + " already belongs to another user"
                        );
                    }
                    // reuse existing vehicle
                    return vehicle;
                })
                .orElseGet(() -> {
                    // create new one
                    Vehicle vehicle = new Vehicle(owner, plateNo, type);
                    owner.addVehicle(vehicle);
                    return vehicleRepository.save(vehicle);
                });
    }


    @Transactional(readOnly = true)
    public Ticket getActiveTicket(String plateNo) {
        Vehicle vehicle = vehicleRepository.findByPlateNo(plateNo)
                .orElseThrow(() -> new NoSuchElementException("Vehicle not found"));

        return ticketRepository.findByVehicleAndStatus(vehicle, TicketStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("No active ticket found"));
    }
}
