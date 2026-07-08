package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class AppointmentRequestDto {
    @NotNull
    private Long shipmentId;
    @NotNull
    private Long bayId;
    @NotNull
    private LocalDateTime scheduledStart;
    @NotNull
    private LocalDateTime scheduledEnd;

    public AppointmentRequestDto() {}
    public Long getShipmentId() { return shipmentId; }
    public void setShipmentId(Long shipmentId) { this.shipmentId = shipmentId; }
    public Long getBayId() { return bayId; }
    public void setBayId(Long bayId) { this.bayId = bayId; }
    public LocalDateTime getScheduledStart() { return scheduledStart; }
    public void setScheduledStart(LocalDateTime scheduledStart) { this.scheduledStart = scheduledStart; }
    public LocalDateTime getScheduledEnd() { return scheduledEnd; }
    public void setScheduledEnd(LocalDateTime scheduledEnd) { this.scheduledEnd = scheduledEnd; }
}
