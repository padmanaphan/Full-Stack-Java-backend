package com.example.demo.controller;

import com.example.demo.dto.DockActivityRequestDto;
import com.example.demo.entity.DockActivityLog;
import com.example.demo.service.DockActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@CrossOrigin(origins = "*")
public class DockActivityController {

    private final DockActivityService dockActivityService;

    public DockActivityController(DockActivityService dockActivityService) {
        this.dockActivityService = dockActivityService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('DOCK_MANAGER', 'WAREHOUSE_OPERATIVE')")
    public ResponseEntity<DockActivityLog> logActivity(@RequestBody DockActivityRequestDto dto, Authentication authentication) {
        return ResponseEntity.ok(dockActivityService.logActivity(dto, authentication.getName()));
    }

    @GetMapping("/appointment/{id}")
    public ResponseEntity<List<DockActivityLog>> getLogs(@PathVariable Long id) {
        return ResponseEntity.ok(dockActivityService.getLogsForAppointment(id));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('DOCK_MANAGER', 'LOGISTICS_COORDINATOR')")
    public ResponseEntity<DockActivityLog> approveLog(@PathVariable Long id) {
        return ResponseEntity.ok(dockActivityService.approveLog(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCK_MANAGER', 'LOGISTICS_COORDINATOR')")
    public ResponseEntity<Void> deleteLog(@PathVariable Long id) {
        dockActivityService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }
}
