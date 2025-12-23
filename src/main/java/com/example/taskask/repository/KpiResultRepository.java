package com.example.taskask.repository;

import com.example.taskask.entity.KpiResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KpiResultRepository extends JpaRepository<KpiResult, Long> {
    List<KpiResult> findByUserId(Long userId);
}
