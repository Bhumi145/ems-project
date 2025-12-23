package com.example.taskask.entity;

import jakarta.persistence.*;

@Entity
public class KpiDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    private String description;
    private String calculationLogic;
    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCalculationLogic() { return calculationLogic; }
    public void setCalculationLogic(String calculationLogic) { this.calculationLogic = calculationLogic; }
}
