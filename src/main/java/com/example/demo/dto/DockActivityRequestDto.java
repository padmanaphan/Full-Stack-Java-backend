package com.example.demo.dto;

public class DockActivityRequestDto {
    private Long appointmentId;
    private String activityType;
    private String notes;

    public DockActivityRequestDto() {}
    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    public String getActivityType() { return activityType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
