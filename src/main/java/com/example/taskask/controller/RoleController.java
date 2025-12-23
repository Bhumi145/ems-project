package com.example.taskask.controller;

import com.example.taskask.entity.Role;
import com.example.taskask.service.RoleService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;
    public RoleController(RoleService roleService) { this.roleService = roleService; }
    @GetMapping
    public List<Role> getAllRoles() { return roleService.getAllRoles(); }
    @PostMapping
    public Role createRole(@RequestBody Role role) { return roleService.saveRole(role); }
}
