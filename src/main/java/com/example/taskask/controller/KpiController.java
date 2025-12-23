package com.example.taskask.controller;

import com.example.taskask.entity.KpiDefinition;
import com.example.taskask.entity.KpiResult;
import com.example.taskask.service.KpiDefinitionService;
import com.example.taskask.service.KpiResultService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/kpi")
public class KpiController {
    private final KpiDefinitionService kpiDefinitionService;
    private final KpiResultService kpiResultService;
    public KpiController(KpiDefinitionService kpiDefinitionService, KpiResultService kpiResultService) {
        this.kpiDefinitionService = kpiDefinitionService;
        this.kpiResultService = kpiResultService;
    }
    @GetMapping("/definitions")
    public List<KpiDefinition> getAllKpiDefinitions() { return kpiDefinitionService.getAllKpis(); }
    @PostMapping("/definitions")
    public KpiDefinition createKpiDefinition(@RequestBody KpiDefinition kpi) { return kpiDefinitionService.save(kpi); }
    @GetMapping("/results/user/{userId}")
    public List<KpiResult> getKpiResultsByUser(@PathVariable Long userId) { return kpiResultService.getResultsByUserId(userId); }
    @PostMapping("/results")
    public KpiResult createKpiResult(@RequestBody KpiResult result) { return kpiResultService.save(result); }
}
