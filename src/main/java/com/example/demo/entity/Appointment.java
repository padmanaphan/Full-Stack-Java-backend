package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Appointment {

    public enum AppointmentStatus {
        SCHEDULED, CHECKED_IN, LOADING, COMPLETED, CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bay_id", nullable = false)
    private LoadingBay bay;

    @Column(name = "scheduled_start", nullable = false)
    private LocalDateTime scheduledStart;

    @Column(name = "scheduled_end", nullable = false)
    private LocalDateTime scheduledEnd;

    @Column(name = "actual_arrival")
    private LocalDateTime actualArrival;

    @Column(name = "completion_time")
    private LocalDateTime completionTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    public Appointment() {
        this.status = AppointmentStatus.SCHEDULED;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Shipment getShipment() { return shipment; }
    public void setShipment(Shipment shipment) { this.shipment = shipment; }
    public LoadingBay getBay() { return bay; }
    public void setBay(LoadingBay bay) { this.bay = bay; }
    public LocalDateTime getScheduledStart() { return scheduledStart; }
    public void setScheduledStart(LocalDateTime scheduledStart) { this.scheduledStart = scheduledStart; }
    public LocalDateTime getScheduledEnd() { return scheduledEnd; }
    public void setScheduledEnd(LocalDateTime scheduledEnd) { this.scheduledEnd = scheduledEnd; }
    public LocalDateTime getActualArrival() { return actualArrival; }
    public void setActualArrival(LocalDateTime actualArrival) { this.actualArrival = actualArrival; }
    public LocalDateTime getCompletionTime() { return completionTime; }
    public void setCompletionTime(LocalDateTime completionTime) { this.completionTime = completionTime; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
}
