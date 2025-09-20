package com.assessment.parkinglot.infra.web;

import com.assessment.parkinglot.application.AdminService;
import com.assessment.parkinglot.domain.entity.ParkingLot;
import com.assessment.parkinglot.domain.entity.ParkingSlot;
import com.assessment.parkinglot.domain.valueobject.VehicleType;
import com.assessment.parkinglot.infra.web.dto.ParkingLotDTO;
import com.assessment.parkinglot.infra.web.dto.ParkingSlotDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // Create a new parking lot
    @PostMapping("/lots")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingLotDTO> createParkingLot(
            @RequestParam String location,
            @RequestParam int floors
    ) {
        ParkingLot lot = adminService.createParkingLot(location, floors);
        return ResponseEntity.ok(ParkingLotDTO.fromEntity(lot));
    }

    // Get all parking lots
    @GetMapping("/lots")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ParkingLotDTO>> getAllParkingLots() {
        List<ParkingLot> lots = adminService.viewAllParkingLots();
        return ResponseEntity.ok(lots.stream()
                .map(ParkingLotDTO::fromEntity)
                .toList());
    }

    // Add slot
    @PostMapping("/slots")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingSlotDTO> addSlot(
            @RequestParam Long lotId,
            @RequestParam VehicleType type,
            @RequestParam int floor
    ) {
        ParkingSlot slot = adminService.addSlot(lotId, type, floor);
        return ResponseEntity.ok(ParkingSlotDTO.fromEntity(slot));
    }

    // Remove slot
    @DeleteMapping("/slots/{slotId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeSlot(@PathVariable Long slotId) {
        adminService.removeSlot(slotId);
        return ResponseEntity.noContent().build();
    }

    // View all slots
    @GetMapping("/slots")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ParkingSlotDTO>> getAllSlots() {
        List<ParkingSlot> slots = adminService.viewAllSlots();
        return ResponseEntity.ok(slots.stream()
                .map(ParkingSlotDTO::fromEntity)
                .toList());
    }
}
