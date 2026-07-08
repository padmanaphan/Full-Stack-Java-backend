package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "loading_bays")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LoadingBay {

    public enum BayType {
        INBOUND, OUTBOUND, COLD_STORAGE
    }

    public enum BayStatus {
        AVAILABLE, OCCUPIED, MAINTENANCE, RESERVED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bay_number", unique = true, nullable = false)
    private String bayNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "bay_type", nullable = false)
    private BayType bayType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BayStatus status = BayStatus.AVAILABLE;

    @Column(name = "max_weight_capacity")
    private BigDecimal maxWeightCapacity;

    @Column(name = "current_weight")
    private BigDecimal currentWeight = BigDecimal.ZERO;

    public LoadingBay() {
        this.status = BayStatus.AVAILABLE;
        this.currentWeight = BigDecimal.ZERO;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBayNumber() { return bayNumber; }
    public void setBayNumber(String bayNumber) { this.bayNumber = bayNumber; }
    public BayType getBayType() { return bayType; }
    public void setBayType(BayType bayType) { this.bayType = bayType; }
    public BayStatus getStatus() { return status; }
    public void setStatus(BayStatus status) { this.status = status; }
    public BigDecimal getMaxWeightCapacity() { return maxWeightCapacity; }
    public void setMaxWeightCapacity(BigDecimal maxWeightCapacity) { this.maxWeightCapacity = maxWeightCapacity; }
    public BigDecimal getCurrentWeight() { return currentWeight; }
    public void setCurrentWeight(BigDecimal currentWeight) { this.currentWeight = currentWeight; }
}
