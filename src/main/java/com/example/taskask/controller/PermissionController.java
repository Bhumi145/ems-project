package com.example.taskask.controller;

import com.example.taskask.entity.Permission;
import com.example.taskask.service.PermissionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    private final PermissionService permissionService;
    public PermissionController(PermissionService permissionService) { this.permissionService = permissionService; }
    @GetMapping
    public List<Permission> getAllPermissions() { return permissionService.getAllPermissions(); }
    @PostMapping
    public Permission createPermission(@RequestBody Permission permission) { return permissionService.savePermission(permission); }
}
