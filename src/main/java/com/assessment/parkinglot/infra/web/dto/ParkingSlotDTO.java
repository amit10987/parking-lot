package com.assessment.parkinglot.infra.web.dto;

import com.assessment.parkinglot.domain.entity.ParkingSlot;
import com.assessment.parkinglot.domain.valueobject.VehicleType;

// Slot response
public record ParkingSlotDTO(
        Long id,
        VehicleType type,
        int floor,
        String status
) {
    public static ParkingSlotDTO fromEntity(ParkingSlot slot) {
        return new ParkingSlotDTO(
                slot.getId(),
                slot.getType(),
                slot.getFloor(),
                slot.getStatus().name()
        );
    }
}
