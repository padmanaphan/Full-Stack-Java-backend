package com.example.demo.controller;

import com.example.demo.dto.AppointmentRequestDto;
import com.example.demo.entity.Appointment;
import com.example.demo.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "https://padmanaphan.github.io")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAll(Authentication authentication) {
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse("");
        return ResponseEntity.ok(appointmentService.getAllAppointments(authentication.getName(), role));
    }

    @PostMapping("/book")
    @PreAuthorize("hasAnyRole('LOGISTICS_COORDINATOR','DOCK_MANAGER')")
    public ResponseEntity<Appointment> book(@Valid @RequestBody AppointmentRequestDto dto) {
        return ResponseEntity.ok(appointmentService.scheduleAppointment(dto));
    }

    @PutMapping("/{id}/check-in")
    @PreAuthorize("hasAnyRole('DOCK_MANAGER', 'CARRIER_PARTNER')")
    public ResponseEntity<Appointment> checkIn(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.checkIn(id));
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('DOCK_MANAGER', 'WAREHOUSE_OPERATIVE')")
    public ResponseEntity<Appointment> complete(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.complete(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICS_COORDINATOR', 'DOCK_MANAGER')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
