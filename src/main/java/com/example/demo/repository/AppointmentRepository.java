package com.example.demo.repository;

import com.example.demo.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByScheduledStartBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT a FROM Appointment a WHERE a.bay.id = :bayId AND a.status IN ('SCHEDULED', 'CHECKED_IN', 'LOADING')")
    List<Appointment> findActiveByBay(@Param("bayId") Long bayId);

    @Query("SELECT a FROM Appointment a WHERE a.shipment.id = :shipmentId AND a.status IN ('SCHEDULED', 'CHECKED_IN', 'LOADING')")
    List<Appointment> findActiveByShipment(@Param("shipmentId") Long shipmentId);

    List<Appointment> findByStatus(Appointment.AppointmentStatus status);
    List<Appointment> findByShipment_CarrierName(String carrierName);
}
