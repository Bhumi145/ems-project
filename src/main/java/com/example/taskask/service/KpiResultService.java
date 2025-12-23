package com.example.taskask.service;

import com.example.taskask.entity.KpiResult;
import com.example.taskask.repository.KpiResultRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class KpiResultService {
    private final KpiResultRepository kpiResultRepository;
    public KpiResultService(KpiResultRepository kpiResultRepository) { this.kpiResultRepository = kpiResultRepository; }
    public List<KpiResult> getResultsByUserId(Long userId) { return kpiResultRepository.findByUserId(userId); }
    public KpiResult save(KpiResult result) { return kpiResultRepository.save(result); }
}
