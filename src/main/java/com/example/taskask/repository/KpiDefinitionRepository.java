package com.example.taskask.repository;

import com.example.taskask.entity.KpiDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface KpiDefinitionRepository extends JpaRepository<KpiDefinition, Long> {
    Optional<KpiDefinition> findByName(String name);
}
