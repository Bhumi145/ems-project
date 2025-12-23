package com.example.taskask.service;

import com.example.taskask.entity.Permission;
import com.example.taskask.repository.PermissionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    public PermissionService(PermissionRepository permissionRepository) { this.permissionRepository = permissionRepository; }
    public List<Permission> getAllPermissions() { return permissionRepository.findAll(); }
    public Optional<Permission> getPermissionByName(String name) { return permissionRepository.findByName(name); }
    public Permission savePermission(Permission permission) { return permissionRepository.save(permission); }
}
