package com.assessment.parkinglot.infra.web;

import com.assessment.parkinglot.application.ParkingService;
import com.assessment.parkinglot.application.VehicleService;
import com.assessment.parkinglot.domain.entity.Ticket;
import com.assessment.parkinglot.domain.entity.User;
import com.assessment.parkinglot.domain.entity.Vehicle;
import com.assessment.parkinglot.domain.valueobject.Receipt;
import com.assessment.parkinglot.domain.valueobject.Role;
import com.assessment.parkinglot.domain.valueobject.VehicleType;
import com.assessment.parkinglot.infra.repository.UserRepository;
import com.assessment.parkinglot.infra.web.dto.TicketDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    private final VehicleService vehicleService;
    private final ParkingService parkingService;
    private final UserRepository userRepository;

    public ParkingController(VehicleService vehicleService, ParkingService parkingService, UserRepository userRepository) {
        this.vehicleService = vehicleService;
        this.parkingService = parkingService;
        this.userRepository = userRepository;
    }

    // Vehicle entry → creates ticket
    @PostMapping("/entry")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TicketDTO> enterVehicle(
            @RequestParam String plateNo,
            @RequestParam VehicleType type,
            @RequestParam Long gateId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        // Extract email from OAuth2 token
        String email = jwt.getClaimAsString("email");

        // Lookup or create User (simplified)
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(new User(email, Role.USER)));

        // Register vehicle (mandatory constructor ensures fields are set)
        Vehicle vehicle = vehicleService.registerVehicle(user, plateNo, type);

        // Park vehicle and generate ticket
        Ticket ticket = parkingService.parkVehicle(vehicle, gateId, "nearest");

        // Return DTO
        return ResponseEntity.ok(TicketDTO.fromEntity(ticket));
    }

    // Vehicle exit → requires payment
    @PostMapping("/exit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Receipt> exitVehicle(
            @RequestParam Long ticketId
    ) {
        Receipt receipt = parkingService.exitVehicle(ticketId);
        return ResponseEntity.ok(receipt);
    }
}
