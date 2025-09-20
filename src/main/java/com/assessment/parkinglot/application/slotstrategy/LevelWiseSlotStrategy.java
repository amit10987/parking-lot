package com.assessment.parkinglot.application.slotstrategy;

import com.assessment.parkinglot.domain.entity.EntryGate;
import com.assessment.parkinglot.domain.entity.ParkingSlot;
import com.assessment.parkinglot.domain.valueobject.SlotStatus;
import com.assessment.parkinglot.domain.valueobject.VehicleType;
import com.assessment.parkinglot.infra.repository.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("levelWise")
public class LevelWiseSlotStrategy implements SlotAllocationStrategy {
    private final ParkingSlotRepository slotRepository;

    public LevelWiseSlotStrategy(ParkingSlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    @Override
    public ParkingSlot findNearestSlot(VehicleType type, EntryGate gate) {
        // First try same floor as gate, then other floors
        List<ParkingSlot> sameFloor = slotRepository.findByTypeAndStatusAndFloor(
            type, SlotStatus.AVAILABLE, gate.getFloor());
        
        if (!sameFloor.isEmpty()) {
            return sameFloor.getFirst();
        }
        
        // Fallback to any available slot
        return slotRepository.findByTypeAndStatus(type, SlotStatus.AVAILABLE)
            .stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Parking lot full for type: " + type));
    }
}