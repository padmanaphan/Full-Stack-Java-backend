package com.example.demo.service;

import com.example.demo.dto.StatsResponseDto;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.LoadingBay;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DockActivityLogRepository;
import com.example.demo.repository.LoadingBayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnalyticsService {

    private final AppointmentRepository appointmentRepository;
    private final DockActivityLogRepository activityLogRepository;
    private final LoadingBayRepository bayRepository;

    public AnalyticsService(AppointmentRepository appointmentRepository,
                            DockActivityLogRepository activityLogRepository,
                            LoadingBayRepository bayRepository) {
        this.appointmentRepository = appointmentRepository;
        this.activityLogRepository = activityLogRepository;
        this.bayRepository = bayRepository;
    }

    @Transactional(readOnly = true)
    public StatsResponseDto getDashboardStats() {
        long total = appointmentRepository.count();
        long active = appointmentRepository.findByStatus(Appointment.AppointmentStatus.LOADING).size()
                + appointmentRepository.findByStatus(Appointment.AppointmentStatus.CHECKED_IN).size();
        long pendingLogs = activityLogRepository.countByApprovedFalse();
        long availableBays = bayRepository.findByStatus(LoadingBay.BayStatus.AVAILABLE).size();
        return StatsResponseDto.builder()
                .totalAppointments(total)
                .activeLoadings(active)
                .pendingLogs(pendingLogs)
                .availableBays(availableBays)
                .build();
    }
}
