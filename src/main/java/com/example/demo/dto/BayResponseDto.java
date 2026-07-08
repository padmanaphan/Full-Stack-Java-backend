package com.example.demo.dto;

import java.math.BigDecimal;

public class BayResponseDto {
    private Long id;
    private String bayNumber;
    private String bayType;
    private String status;
    private BigDecimal maxWeightCapacity;

    public BayResponseDto() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBayNumber() { return bayNumber; }
    public void setBayNumber(String bayNumber) { this.bayNumber = bayNumber; }
    public String getBayType() { return bayType; }
    public void setBayType(String bayType) { this.bayType = bayType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getMaxWeightCapacity() { return maxWeightCapacity; }
    public void setMaxWeightCapacity(BigDecimal maxWeightCapacity) { this.maxWeightCapacity = maxWeightCapacity; }
}
