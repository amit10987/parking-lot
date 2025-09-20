package com.assessment.parkinglot.infra.web.dto;

import com.assessment.parkinglot.domain.entity.Ticket;

import java.time.LocalDateTime;

// Ticket response
public record TicketDTO(
        Long id,
        String plateNo,
        String slotId,
        LocalDateTime entryTime,
        LocalDateTime exitTime,
        String status
) {
    public static TicketDTO fromEntity(Ticket ticket) {
        return new TicketDTO(
                ticket.getId(),
                ticket.getVehicle().getPlateNo(),
                ticket.getSlot().getId().toString(),
                ticket.getEntryTime(),
                ticket.getExitTime(),
                ticket.getStatus().name()
        );
    }
}



