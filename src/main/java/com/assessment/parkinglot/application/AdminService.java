package com.assessment.parkinglot.application;

import com.assessment.parkinglot.domain.entity.ParkingLot;
import com.assessment.parkinglot.domain.entity.ParkingSlot;
import com.assessment.parkinglot.domain.valueobject.VehicleType;
import com.assessment.parkinglot.infra.repository.ParkingLotRepository;
import com.assessment.parkinglot.infra.repository.ParkingSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AdminService {

    private final ParkingSlotRepository slotRepository;
    private final ParkingLotRepository lotRepository;

    public AdminService(ParkingSlotRepository slotRepository, ParkingLotRepository lotRepository) {
        this.slotRepository = slotRepository;
        this.lotRepository = lotRepository;
    }

    // ------------------- ParkingLot Operations -------------------

    @Transactional
    public ParkingLot createParkingLot(String location, int floors) {
        ParkingLot lot = new ParkingLot(location, floors);
        // Add default entry gates: one per floor
        for (int i = 0; i < floors; i++) {
            lot.addGate(i);
        }
        return lotRepository.save(lot);
    }

    @Transactional(readOnly = true)
    public List<ParkingLot> viewAllParkingLots() {
        return lotRepository.findAll();
    }

    @Transactional
    public void removeParkingLot(Long lotId) {
        ParkingLot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> new NoSuchElementException("Parking lot not found"));
        lotRepository.delete(lot);
    }

    // ------------------- ParkingSlot Operations -------------------

    @Transactional
    public ParkingSlot addSlot(Long lotId, VehicleType type, int floor) {
        ParkingLot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> new NoSuchElementException("Parking lot not found"));

        ParkingSlot slot = lot.addSlot(type, floor); // entity method creates slot and sets status
        return slotRepository.save(slot);
    }

    @Transactional
    public void removeSlot(Long slotId) {
        ParkingSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new NoSuchElementException("Slot not found"));

        slot.getParkingLot().removeSlot(slot); // entity handles deactivation/removal
        slotRepository.save(slot);
    }

    @Transactional(readOnly = true)
    public List<ParkingSlot> viewAllSlots() {
        return slotRepository.findAll();
    }
}
