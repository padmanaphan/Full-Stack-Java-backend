package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "shipments")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Shipment {

    public enum ShipmentPriority {
        CRITICAL, EXPEDITED, STANDARD
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shipment_number", unique = true, nullable = false)
    private String shipmentNumber;

    @Column(name = "carrier_name", nullable = false)
    private String carrierName;

    @Column(name = "total_units", nullable = false)
    private Integer totalUnits;

    @Column(name = "estimated_weight")
    private BigDecimal estimatedWeight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShipmentPriority priority = ShipmentPriority.STANDARD;

    @Column(name = "hazardous_material")
    private boolean hazardousMaterial = false;

    public Shipment() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getShipmentNumber() { return shipmentNumber; }
    public void setShipmentNumber(String shipmentNumber) { this.shipmentNumber = shipmentNumber; }
    public String getCarrierName() { return carrierName; }
    public void setCarrierName(String carrierName) { this.carrierName = carrierName; }
    public Integer getTotalUnits() { return totalUnits; }
    public void setTotalUnits(Integer totalUnits) { this.totalUnits = totalUnits; }
    public BigDecimal getEstimatedWeight() { return estimatedWeight; }
    public void setEstimatedWeight(BigDecimal estimatedWeight) { this.estimatedWeight = estimatedWeight; }
    public ShipmentPriority getPriority() { return priority; }
    public void setPriority(ShipmentPriority priority) { this.priority = priority; }
    public boolean isHazardousMaterial() { return hazardousMaterial; }
    public void setHazardousMaterial(boolean hazardousMaterial) { this.hazardousMaterial = hazardousMaterial; }
}
