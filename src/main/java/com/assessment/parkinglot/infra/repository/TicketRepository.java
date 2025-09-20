package com.assessment.parkinglot.infra.repository;

import com.assessment.parkinglot.domain.entity.Ticket;
import com.assessment.parkinglot.domain.entity.Vehicle;
import com.assessment.parkinglot.domain.valueobject.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByVehicleAndStatus(Vehicle vehicle, TicketStatus status);
}
