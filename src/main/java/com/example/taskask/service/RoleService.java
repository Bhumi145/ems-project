package com.example.taskask.service;

import com.example.taskask.entity.Role;
import com.example.taskask.repository.RoleRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    public RoleService(RoleRepository roleRepository) { this.roleRepository = roleRepository; }
    public List<Role> getAllRoles() { return roleRepository.findAll(); }
    public Optional<Role> getRoleByName(String name) { return roleRepository.findByName(name); }
    public Role saveRole(Role role) { return roleRepository.save(role); }
}
