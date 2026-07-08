package com.example.demo.service;

import com.example.demo.dto.AppointmentRequestDto;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.LoadingBay;
import com.example.demo.entity.Shipment;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.LoadingBayRepository;
import com.example.demo.repository.ShipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private static final BigDecimal DEFAULT_WEIGHT = new BigDecimal("500.00");

    private final AppointmentRepository appointmentRepository;
    private final LoadingBayRepository bayRepository;
    private final ShipmentRepository shipmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              LoadingBayRepository bayRepository,
                              ShipmentRepository shipmentRepository) {
        this.appointmentRepository = appointmentRepository;
        this.bayRepository = bayRepository;
        this.shipmentRepository = shipmentRepository;
    }

    public List<Appointment> getAllAppointments(String username, String role) {
        if ("CARRIER_PARTNER".equals(role)) {
            return appointmentRepository.findByShipment_CarrierName(username);
        }
        return appointmentRepository.findAll();
    }

    @Transactional
    public Appointment scheduleAppointment(AppointmentRequestDto dto) {
        LoadingBay bay = bayRepository.findById(dto.getBayId())
                .orElseThrow(() -> new ResourceNotFoundException("Bay not found"));
        Shipment shipment = shipmentRepository.findById(dto.getShipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found"));

        BigDecimal projectedWeight = shipment.getEstimatedWeight() == null ? DEFAULT_WEIGHT : shipment.getEstimatedWeight();
        for (Appointment active : appointmentRepository.findActiveByBay(bay.getId())) {
            BigDecimal weight = active.getShipment().getEstimatedWeight() == null ? DEFAULT_WEIGHT : active.getShipment().getEstimatedWeight();
            projectedWeight = projectedWeight.add(weight);
        }
        if (bay.getMaxWeightCapacity() != null && projectedWeight.compareTo(bay.getMaxWeightCapacity()) > 0) {
            throw new IllegalStateException("Bay capacity exceeded with this shipment");
        }
        if (!appointmentRepository.findActiveByShipment(shipment.getId()).isEmpty()) {
            throw new IllegalStateException("Shipment is already scheduled for another bay or in progress");
        }

        Appointment appointment = new Appointment();
        appointment.setBay(bay);
        appointment.setShipment(shipment);
        appointment.setScheduledStart(dto.getScheduledStart());
        appointment.setScheduledEnd(dto.getScheduledEnd());
        appointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);
        Appointment saved = appointmentRepository.save(appointment);
        refreshBayStatus(bay);
        return saved;
    }

    @Transactional
    public Appointment checkIn(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        appointment.setActualArrival(LocalDateTime.now());
        appointment.setStatus(Appointment.AppointmentStatus.CHECKED_IN);
        Appointment saved = appointmentRepository.save(appointment);
        refreshBayStatus(saved.getBay());
        return saved;
    }

    @Transactional
    public Appointment complete(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        if (appointment.getStatus() == Appointment.AppointmentStatus.SCHEDULED) {
            throw new IllegalStateException("Appointment must be Checked-In before completion");
        }
        appointment.setCompletionTime(LocalDateTime.now());
        appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
        Appointment saved = appointmentRepository.save(appointment);
        refreshBayStatus(saved.getBay());
        return saved;
    }

    @Transactional
    public void refreshBayStatus(LoadingBay bay) {
        if (bay.getStatus() == LoadingBay.BayStatus.MAINTENANCE) {
            return;
        }
        List<Appointment> activeAppointments = appointmentRepository.findActiveByBay(bay.getId());
        BigDecimal currentWeight = BigDecimal.ZERO;
        boolean hasCheckedInOrLoading = false;
        for (Appointment active : activeAppointments) {
            BigDecimal weight = active.getShipment().getEstimatedWeight() == null ? DEFAULT_WEIGHT : active.getShipment().getEstimatedWeight();
            currentWeight = currentWeight.add(weight);
            if (active.getStatus() == Appointment.AppointmentStatus.CHECKED_IN || active.getStatus() == Appointment.AppointmentStatus.LOADING) {
                hasCheckedInOrLoading = true;
            }
        }
        bay.setCurrentWeight(currentWeight);
        if (hasCheckedInOrLoading) {
            bay.setStatus(LoadingBay.BayStatus.OCCUPIED);
        } else if (!activeAppointments.isEmpty()) {
            bay.setStatus(LoadingBay.BayStatus.RESERVED);
        } else {
            bay.setStatus(LoadingBay.BayStatus.AVAILABLE);
        }
        bayRepository.save(bay);
    }

    @Transactional
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        LoadingBay bay = appointment.getBay();
        appointmentRepository.delete(appointment);
        if (bay != null) {
            refreshBayStatus(bay);
        }
    }
}
