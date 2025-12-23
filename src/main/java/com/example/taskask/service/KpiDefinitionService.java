package com.example.taskask.service;

import com.example.taskask.entity.KpiDefinition;
import com.example.taskask.repository.KpiDefinitionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class KpiDefinitionService {
    private final KpiDefinitionRepository kpiDefinitionRepository;
    public KpiDefinitionService(KpiDefinitionRepository kpiDefinitionRepository) { this.kpiDefinitionRepository = kpiDefinitionRepository; }
    public List<KpiDefinition> getAllKpis() { return kpiDefinitionRepository.findAll(); }
    public Optional<KpiDefinition> getKpiByName(String name) { return kpiDefinitionRepository.findByName(name); }
    public KpiDefinition save(KpiDefinition kpi) { return kpiDefinitionRepository.save(kpi); }
}
