package com.example.demo.controller;

import com.example.demo.dto.StatsResponseDto;
import com.example.demo.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('LOGISTICS_COORDINATOR', 'DOCK_MANAGER', 'WAREHOUSE_OPERATIVE')")
    public ResponseEntity<StatsResponseDto> getDashboardStats() {
        return ResponseEntity.ok(analyticsService.getDashboardStats());
    }
}
