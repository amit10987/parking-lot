package com.assessment.parkinglot.application.slotstrategy;

import com.assessment.parkinglot.domain.entity.EntryGate;
import com.assessment.parkinglot.domain.entity.ParkingSlot;
import com.assessment.parkinglot.domain.valueobject.SlotStatus;
import com.assessment.parkinglot.domain.valueobject.VehicleType;
import com.assessment.parkinglot.infra.repository.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Qualifier("nearest")
public class NearestSlotStrategy implements SlotAllocationStrategy {

    private final ParkingSlotRepository slotRepository;

    public NearestSlotStrategy(ParkingSlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    @Override
    public ParkingSlot findNearestSlot(VehicleType type, EntryGate gate) {
        // Fetch available slots for vehicle type
        List<ParkingSlot> available = slotRepository.findByTypeAndStatusWithLock(type, SlotStatus.AVAILABLE);

        if (available.isEmpty()) {
            throw new RuntimeException("Parking lot full for type: " + type);
        }

        // Pick nearest based on distanceFromGate or floor difference
        return available.stream()
                        .min(Comparator.comparingInt(slot -> Math.abs(slot.getFloor() - gate.getFloor())))
                        .orElseThrow();
    }
}