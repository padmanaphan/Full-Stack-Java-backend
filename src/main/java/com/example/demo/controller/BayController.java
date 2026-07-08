package com.example.demo.controller;

import com.example.demo.entity.LoadingBay;
import com.example.demo.service.BayService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/bays")
@CrossOrigin(origins = "https://padmanaphan.github.io")
public class BayController {

    private final BayService bayService;

    public BayController(BayService bayService) {
        this.bayService = bayService;
    }

    @GetMapping
    public ResponseEntity<List<LoadingBay>> getAllBays() {
        return ResponseEntity.ok(bayService.getAllBays());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('LOGISTICS_COORDINATOR','DOCK_MANAGER')")
    public ResponseEntity<LoadingBay> createBay(@RequestBody LoadingBay bay) {
        return ResponseEntity.ok(bayService.createBay(bay));
    }

    @PatchMapping("/{id}/weight")
    @PreAuthorize("hasAnyRole('DOCK_MANAGER', 'WAREHOUSE_OPERATIVE')")
    public ResponseEntity<LoadingBay> updateWeight(@PathVariable Long id, @RequestBody BigDecimal weight) {
        return ResponseEntity.ok(bayService.updateWeight(id, weight));
    }

    @PutMapping("/{id}/maintenance")
    @PreAuthorize("hasAnyRole('LOGISTICS_COORDINATOR', 'DOCK_MANAGER')")
    public ResponseEntity<LoadingBay> updateMaintenance(@PathVariable Long id, @RequestParam LoadingBay.BayStatus status) {
        return ResponseEntity.ok(bayService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICS_COORDINATOR', 'DOCK_MANAGER')")
    public ResponseEntity<Void> deleteBay(@PathVariable Long id) {
        bayService.deleteBay(id);
        return ResponseEntity.noContent().build();
    }
}
