package com.example.demo.controller;

import com.example.demo.entity.Shipment;
import com.example.demo.service.ShipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@CrossOrigin(origins = "https://padmanaphan.github.io")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('LOGISTICS_COORDINATOR','DOCK_MANAGER')")
    public ResponseEntity<Shipment> createShipment(@RequestBody Shipment shipment) {
        return ResponseEntity.ok(shipmentService.createShipment(shipment));
    }

    @GetMapping
    public ResponseEntity<List<Shipment>> getShipments(@RequestParam(required = false) Boolean available) {
        if (Boolean.TRUE.equals(available)) {
            return ResponseEntity.ok(shipmentService.getAvailableShipments());
        }
        return ResponseEntity.ok(shipmentService.getAllShipments());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICS_COORDINATOR', 'DOCK_MANAGER')")
    public ResponseEntity<Void> deleteShipment(@PathVariable Long id) {
        shipmentService.deleteShipment(id);
        return ResponseEntity.noContent().build();
    }
}
