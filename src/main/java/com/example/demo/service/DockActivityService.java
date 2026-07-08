package com.example.demo.service;

import com.example.demo.dto.DockActivityRequestDto;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.DockActivityLog;
import com.example.demo.entity.SystemUser;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DockActivityLogRepository;
import com.example.demo.repository.SystemUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DockActivityService {

    private final DockActivityLogRepository activityLogRepository;
    private final AppointmentRepository appointmentRepository;
    private final SystemUserRepository userRepository;
    private final AppointmentService appointmentService;

    public DockActivityService(DockActivityLogRepository activityLogRepository,
                               AppointmentRepository appointmentRepository,
                               SystemUserRepository userRepository,
                               AppointmentService appointmentService) {
        this.activityLogRepository = activityLogRepository;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.appointmentService = appointmentService;
    }

    @Transactional
    public DockActivityLog logActivity(DockActivityRequestDto dto, String username) {
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        SystemUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED || appointment.getStatus() == Appointment.AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Cannot log activity for a finalized appointment.");
        }

        String type = dto.getActivityType() == null ? "" : dto.getActivityType().toUpperCase();
        boolean autoApprove = user.getRole() == SystemUser.UserRole.DOCK_MANAGER || user.getRole() == SystemUser.UserRole.LOGISTICS_COORDINATOR;

        if (type.contains("INSPECTION")) {
            boolean hasInspectionResult = activityLogRepository.findByAppointmentIdOrderByTimestampDesc(appointment.getId()).stream()
                    .map(DockActivityLog::getActivityType)
                    .filter(v -> v != null)
                    .map(String::toUpperCase)
                    .anyMatch(v -> v.contains("INSPECTION_PASSED") || v.contains("INSPECTION_FAILED"));
            if (hasInspectionResult) {
                throw new IllegalStateException("An inspection result (PASSED/FAILED) already exists for this appointment.");
            }
        }

        if (type.contains("PROGRESS") || type.contains("SCANNING")) {
            if (appointment.getStatus() == Appointment.AppointmentStatus.SCHEDULED) {
                throw new IllegalStateException("Cannot log loading progress before Check-In");
            }
            appointment.setStatus(Appointment.AppointmentStatus.LOADING);
            appointmentRepository.save(appointment);
            appointmentService.refreshBayStatus(appointment.getBay());
        } else if ("CANCEL_APPOINTMENT".equals(type)) {
            appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
            appointmentRepository.save(appointment);
            appointmentService.refreshBayStatus(appointment.getBay());
        }

        DockActivityLog log = new DockActivityLog();
        log.setAppointment(appointment);
        log.setPerformedBy(user);
        log.setActivityType(dto.getActivityType());
        log.setNotes(dto.getNotes());
        log.setTimestamp(LocalDateTime.now());
        log.setApproved(autoApprove);
        return activityLogRepository.save(log);
    }

    @Transactional
    public DockActivityLog approveLog(Long logId) {
        DockActivityLog log = activityLogRepository.findById(logId)
                .orElseThrow(() -> new ResourceNotFoundException("Log not found"));
        log.setApproved(true);
        return activityLogRepository.save(log);
    }

    public List<DockActivityLog> getLogsForAppointment(Long appointmentId) {
        return activityLogRepository.findByAppointmentIdOrderByTimestampDesc(appointmentId);
    }

    @Transactional
    public void deleteLog(Long logId) {
        DockActivityLog log = activityLogRepository.findById(logId)
                .orElseThrow(() -> new ResourceNotFoundException("Log not found"));
        activityLogRepository.delete(log);
    }
}
