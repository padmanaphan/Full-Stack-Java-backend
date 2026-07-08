package com.example.demo.repository;

import com.example.demo.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Optional<Shipment> findByShipmentNumber(String shipmentNumber);

    @Query("SELECT s FROM Shipment s WHERE s.id NOT IN (SELECT a.shipment.id FROM Appointment a WHERE a.status IN ('SCHEDULED', 'CHECKED_IN', 'LOADING'))")
    List<Shipment> findAvailableShipments();
}
