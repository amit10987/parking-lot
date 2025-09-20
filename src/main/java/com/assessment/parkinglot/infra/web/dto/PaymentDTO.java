package com.assessment.parkinglot.infra.web.dto;

import com.assessment.parkinglot.domain.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Payment response
public record PaymentDTO(
        Long id,
        Long ticketId,
        BigDecimal amount,
        String status,
        LocalDateTime timestamp
) {
    public static PaymentDTO fromEntity(Payment payment) {
        return new PaymentDTO(
                payment.getId(),
                payment.getTicket().getId(),
                payment.getAmount(),
                payment.getStatus().name(),
                payment.getTimestamp()
        );
    }
}