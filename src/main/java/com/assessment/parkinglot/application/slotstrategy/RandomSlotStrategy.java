package com.assessment.parkinglot.application.slotstrategy;

import com.assessment.parkinglot.domain.entity.EntryGate;
import com.assessment.parkinglot.domain.entity.ParkingSlot;
import com.assessment.parkinglot.domain.valueobject.SlotStatus;
import com.assessment.parkinglot.domain.valueobject.VehicleType;
import com.assessment.parkinglot.infra.repository.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Qualifier("random")
public class RandomSlotStrategy implements SlotAllocationStrategy {
    private final ParkingSlotRepository slotRepository;
    private final Random random = new Random();

    public RandomSlotStrategy(ParkingSlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    @Override
    public ParkingSlot findNearestSlot(VehicleType type, EntryGate gate) {
        List<ParkingSlot> available = slotRepository.findByTypeAndStatus(type, SlotStatus.AVAILABLE);
        if (available.isEmpty()) {
            throw new RuntimeException("Parking lot full for type: " + type);
        }
        return available.get(random.nextInt(available.size()));
    }
}