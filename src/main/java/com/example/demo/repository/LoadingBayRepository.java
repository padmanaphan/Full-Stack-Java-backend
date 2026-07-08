package com.example.demo.repository;

import com.example.demo.entity.LoadingBay;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoadingBayRepository extends JpaRepository<LoadingBay, Long> {
    List<LoadingBay> findByStatus(LoadingBay.BayStatus status);
    boolean existsByBayNumber(String bayNumber);
}
