package com.example.demo.config;

import com.example.demo.entity.LoadingBay;
import com.example.demo.entity.Shipment;
import com.example.demo.entity.SystemUser;
import com.example.demo.repository.LoadingBayRepository;
import com.example.demo.repository.ShipmentRepository;
import com.example.demo.repository.SystemUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    private final SystemUserRepository userRepository;
    private final LoadingBayRepository bayRepository;
    private final ShipmentRepository shipmentRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(SystemUserRepository userRepository,
                      LoadingBayRepository bayRepository,
                      ShipmentRepository shipmentRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bayRepository = bayRepository;
        this.shipmentRepository = shipmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedUser("admin", "admin@dockdash.com", "admin123", SystemUser.UserRole.LOGISTICS_COORDINATOR);
        seedUser("manager", "manager@dockdash.com", "manager123", SystemUser.UserRole.DOCK_MANAGER);
        seedUser("operative", "operative@dockdash.com", "operative123", SystemUser.UserRole.WAREHOUSE_OPERATIVE);
        seedUser("SKA LOGISTICS", "ska@dockdash.com", "ska123", SystemUser.UserRole.CARRIER_PARTNER);

        if (!bayRepository.existsByBayNumber("BAY-101")) {
            LoadingBay bay = new LoadingBay();
            bay.setBayNumber("BAY-101");
            bay.setBayType(LoadingBay.BayType.INBOUND);
            bay.setStatus(LoadingBay.BayStatus.AVAILABLE);
            bay.setMaxWeightCapacity(new BigDecimal("10000"));
            bayRepository.save(bay);
        }
        if (!shipmentRepository.findByShipmentNumber("SHIP-1001").isPresent()) {
            Shipment shipment = new Shipment();
            shipment.setShipmentNumber("SHIP-1001");
            shipment.setCarrierName("SKA LOGISTICS");
            shipment.setTotalUnits(25);
            shipment.setEstimatedWeight(new BigDecimal("2500"));
            shipment.setPriority(Shipment.ShipmentPriority.STANDARD);
            shipmentRepository.save(shipment);
        }
    }

    private void seedUser(String username, String email, String password, SystemUser.UserRole role) {
        if (!userRepository.existsByUsername(username)) {
            SystemUser user = new SystemUser();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            user.setActive(true);
            userRepository.save(user);
        }
    }
}
