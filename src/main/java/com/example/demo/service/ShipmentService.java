package com.example.demo.service;

import com.example.demo.entity.Shipment;
import com.example.demo.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public Shipment createShipment(Shipment shipment) {
        if (shipment.isHazardousMaterial()) {
            shipment.setPriority(Shipment.ShipmentPriority.CRITICAL);
        } else if (shipment.getTotalUnits() != null && shipment.getTotalUnits() > 50) {
            shipment.setPriority(Shipment.ShipmentPriority.EXPEDITED);
        } else {
            shipment.setPriority(Shipment.ShipmentPriority.STANDARD);
        }
        return shipmentRepository.save(shipment);
    }

    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    public List<Shipment> getAvailableShipments() {
        return shipmentRepository.findAvailableShipments();
    }

    public void deleteShipment(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new com.example.demo.exception.ResourceNotFoundException("Shipment not found"));
        shipmentRepository.delete(shipment);
    }
}
