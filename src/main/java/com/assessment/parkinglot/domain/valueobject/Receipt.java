package com.assessment.parkinglot.domain.valueobject;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Receipt {

    private String vehiclePlateNo;
    private Long slotId;
    private BigDecimal amount;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

    // Constructor
    public Receipt(String vehiclePlateNo, Long slotId, BigDecimal amount,
                   LocalDateTime entryTime, LocalDateTime exitTime) {
        this.vehiclePlateNo = vehiclePlateNo;
        this.slotId = slotId;
        this.amount = amount;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
    }

    // Getters
    public String getVehiclePlateNo() {
        return vehiclePlateNo;
    }

    public Long getSlotId() {
        return slotId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    // Optional: toString() for easy logging or response
    @Override
    public String toString() {
        return "Receipt{" +
                "vehiclePlateNo='" + vehiclePlateNo + '\'' +
                ", slotId=" + slotId +
                ", amount=" + amount +
                ", entryTime=" + entryTime +
                ", exitTime=" + exitTime +
                '}';
    }
}
