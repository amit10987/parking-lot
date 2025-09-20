package com.assessment.parkinglot.application.slotstrategy;

import com.assessment.parkinglot.domain.entity.EntryGate;
import com.assessment.parkinglot.domain.entity.ParkingSlot;
import com.assessment.parkinglot.domain.valueobject.VehicleType;

public interface SlotAllocationStrategy {
    ParkingSlot findNearestSlot(VehicleType type, EntryGate gate);
}