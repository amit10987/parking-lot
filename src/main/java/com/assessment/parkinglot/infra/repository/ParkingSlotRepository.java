package com.assessment.parkinglot.infra.repository;

import com.assessment.parkinglot.domain.entity.ParkingSlot;
import com.assessment.parkinglot.domain.valueobject.SlotStatus;
import com.assessment.parkinglot.domain.valueobject.VehicleType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    List<ParkingSlot> findByTypeAndStatus(VehicleType type, SlotStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM ParkingSlot p WHERE p.type = :type AND p.status = :status")
    List<ParkingSlot> findByTypeAndStatusWithLock(@Param("type") VehicleType type, @Param("status") SlotStatus status);

    // For level-wise strategy
    List<ParkingSlot> findByTypeAndStatusAndFloor(VehicleType type, SlotStatus status, int floor);

    // For first-available strategy
    List<ParkingSlot> findByTypeAndStatusOrderById(VehicleType type, SlotStatus status);
}
