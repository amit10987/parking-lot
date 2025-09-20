package com.assessment.parkinglot.infra.repository;

import com.assessment.parkinglot.domain.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByPlateNo(String plateNo);
    boolean existsByPlateNoAndOwnerId(String plateNo, Long ownerId);
}
