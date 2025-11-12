package com.alexandersaul.Alexander.service.impl;

import com.alexandersaul.Alexander.dto.rolepermission.RolePermissionRegisterDto;
import com.alexandersaul.Alexander.dto.rolepermission.RolePermissionResponseDto;
import com.alexandersaul.Alexander.entity.Permission;
import com.alexandersaul.Alexander.entity.Role;
import com.alexandersaul.Alexander.entity.RolePermission;
import com.alexandersaul.Alexander.entity.RolePermissionKey;
import com.alexandersaul.Alexander.mapper.RolePermissionMapper;
import com.alexandersaul.Alexander.repository.PermissionRepository;
import com.alexandersaul.Alexander.repository.RolePermissionRepository;
import com.alexandersaul.Alexander.repository.RoleRepository;
import com.alexandersaul.Alexander.service.RolePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RolePermissionMapper rolePermissionMapper;

    @Override
    public List<RolePermissionResponseDto> findAll() {
        List<RolePermission> rolePermissionList = rolePermissionRepository.findAll();
        return rolePermissionMapper.toListResponseDto(rolePermissionList);
    }

    @Override
    public RolePermissionResponseDto save(RolePermissionRegisterDto rolePermissionRegisterDto) {
        RolePermission rolePermission = rolePermissionRepository.save(buildRolePermission(rolePermissionRegisterDto));
        return rolePermissionMapper.toResponseDto(rolePermission);
    }

    private RolePermission buildRolePermission (RolePermissionRegisterDto rolePermissionRegisterDto){
        Role role = roleRepository.findById(rolePermissionRegisterDto.getRoleId()).orElseThrow(() -> new RuntimeException("Role not founded"));
        Permission permission = permissionRepository.findById(rolePermissionRegisterDto.getPermissionId()).orElseThrow(() -> new RuntimeException("Permission not founded"));
        RolePermissionKey rolePermissionKey = buildRolePermissionKey(rolePermissionRegisterDto);
        return RolePermission.builder()
                .rolePermissionKey(rolePermissionKey)
                .role(role)
                .permission(permission)
                .build();
    }

    private RolePermissionKey buildRolePermissionKey (RolePermissionRegisterDto rolePermissionRegisterDto){
        return RolePermissionKey.builder()
                .permissionId(rolePermissionRegisterDto.getPermissionId())
                .roleId(rolePermissionRegisterDto.getRoleId())
                .build();
    }
}