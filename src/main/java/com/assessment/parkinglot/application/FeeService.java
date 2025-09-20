package com.assessment.parkinglot.application;

import com.assessment.parkinglot.domain.valueobject.VehicleType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class FeeService {

    // Example: configurable base rates
    private final Map<VehicleType, BigDecimal> ratePerHour = Map.of(
            VehicleType.BIKE, BigDecimal.valueOf(5),   // or new BigDecimal("5.00")
            VehicleType.CAR, BigDecimal.valueOf(10),  // or new BigDecimal("10.00")
            VehicleType.TRUCK, BigDecimal.valueOf(20)  // or new BigDecimal("20.00")
    );

    public BigDecimal calculateFee(LocalDateTime entryTime, LocalDateTime exitTime, VehicleType type) {
        Duration duration = Duration.between(entryTime, exitTime);
        // first 2 hours free
        long freeHours = 2;
        long hours = Math.max(0, duration.toHours() - freeHours);
        BigDecimal rate = ratePerHour.getOrDefault(type, BigDecimal.valueOf(10.0));
        return rate.multiply(BigDecimal.valueOf(hours));
    }
}
