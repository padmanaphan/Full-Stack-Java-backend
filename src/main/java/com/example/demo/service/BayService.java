package com.example.demo.service;

import com.example.demo.entity.LoadingBay;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.LoadingBayRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BayService {

    private final LoadingBayRepository bayRepository;

    public BayService(LoadingBayRepository bayRepository) {
        this.bayRepository = bayRepository;
    }

    public List<LoadingBay> getAllBays() {
        return bayRepository.findAll();
    }

    public LoadingBay createBay(LoadingBay bay) {
        if (bay.getStatus() == null) {
            bay.setStatus(LoadingBay.BayStatus.AVAILABLE);
        }
        if (bay.getCurrentWeight() == null) {
            bay.setCurrentWeight(BigDecimal.ZERO);
        }
        return bayRepository.save(bay);
    }

    public LoadingBay updateStatus(Long id, LoadingBay.BayStatus status) {
        LoadingBay bay = getBayById(id);
        bay.setStatus(status);
        return bayRepository.save(bay);
    }

    public LoadingBay updateWeight(Long id, BigDecimal newWeight) {
        LoadingBay bay = getBayById(id);
        bay.setCurrentWeight(newWeight == null ? BigDecimal.ZERO : newWeight);
        if (bay.getMaxWeightCapacity() != null && bay.getCurrentWeight().compareTo(bay.getMaxWeightCapacity()) >= 0) {
            bay.setStatus(LoadingBay.BayStatus.OCCUPIED);
        } else if (bay.getStatus() == LoadingBay.BayStatus.OCCUPIED) {
            bay.setStatus(LoadingBay.BayStatus.AVAILABLE);
        }
        return bayRepository.save(bay);
    }

    public LoadingBay getBayById(Long id) {
        return bayRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bay not found"));
    }

    public void deleteBay(Long id) {
        LoadingBay bay = getBayById(id);
        bayRepository.delete(bay);
    }
}
