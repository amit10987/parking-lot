package com.assessment.parkinglot.infra.web.dto;

import com.assessment.parkinglot.domain.entity.ParkingLot;

import java.util.List;

public record ParkingLotDTO(
        Long id,
        String location,
        int floors,
        List<ParkingSlotDTO> slots
) {
    public static ParkingLotDTO fromEntity(ParkingLot lot) {
        return new ParkingLotDTO(
                lot.getId(),
                lot.getLocation(),
                lot.getFloors(),
                lot.getSlots() != null ? lot.getSlots().stream()
                        .map(ParkingSlotDTO::fromEntity)
                        .toList() : List.of()
        );
    }
}
