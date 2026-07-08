package com.example.demo.repository;

import com.example.demo.entity.DockActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DockActivityLogRepository extends JpaRepository<DockActivityLog, Long> {
    List<DockActivityLog> findByAppointmentIdOrderByTimestampDesc(Long appointmentId);
    long countByApprovedFalse();
}
