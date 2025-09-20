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
@Qualifier("firstAvailable")
public class FirstAvailableSlotStrategy implements SlotAllocationStrategy {
    private final ParkingSlotRepository slotRepository;

    public FirstAvailableSlotStrategy(ParkingSlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    @Override
    public ParkingSlot findNearestSlot(VehicleType type, EntryGate gate) {
        List<ParkingSlot> available = slotRepository.findByTypeAndStatusOrderById(type, SlotStatus.AVAILABLE);
        return available.stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Parking lot full for type: " + type));
    }
}