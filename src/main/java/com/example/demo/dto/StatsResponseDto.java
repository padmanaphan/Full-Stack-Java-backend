package com.example.demo.dto;

public class StatsResponseDto {
    private long totalAppointments;
    private long activeLoadings;
    private long pendingLogs;
    private long availableBays;

    public StatsResponseDto() {}
    public StatsResponseDto(long totalAppointments, long activeLoadings, long pendingLogs, long availableBays) {
        this.totalAppointments = totalAppointments;
        this.activeLoadings = activeLoadings;
        this.pendingLogs = pendingLogs;
        this.availableBays = availableBays;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private long totalAppointments;
        private long activeLoadings;
        private long pendingLogs;
        private long availableBays;
        public Builder totalAppointments(long totalAppointments) { this.totalAppointments = totalAppointments; return this; }
        public Builder activeLoadings(long activeLoadings) { this.activeLoadings = activeLoadings; return this; }
        public Builder pendingLogs(long pendingLogs) { this.pendingLogs = pendingLogs; return this; }
        public Builder availableBays(long availableBays) { this.availableBays = availableBays; return this; }
        public StatsResponseDto build() { return new StatsResponseDto(totalAppointments, activeLoadings, pendingLogs, availableBays); }
    }

    public long getTotalAppointments() { return totalAppointments; }
    public void setTotalAppointments(long totalAppointments) { this.totalAppointments = totalAppointments; }
    public long getActiveLoadings() { return activeLoadings; }
    public void setActiveLoadings(long activeLoadings) { this.activeLoadings = activeLoadings; }
    public long getPendingLogs() { return pendingLogs; }
    public void setPendingLogs(long pendingLogs) { this.pendingLogs = pendingLogs; }
    public long getAvailableBays() { return availableBays; }
    public void setAvailableBays(long availableBays) { this.availableBays = availableBays; }
}
