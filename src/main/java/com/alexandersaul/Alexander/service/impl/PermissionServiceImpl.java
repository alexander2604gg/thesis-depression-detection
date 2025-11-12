package com.alexandersaul.Alexander.service.impl;


import com.alexandersaul.Alexander.dto.permission.PermissionRegisterDto;
import com.alexandersaul.Alexander.dto.permission.PermissionResponseDto;
import com.alexandersaul.Alexander.entity.Permission;
import com.alexandersaul.Alexander.mapper.PermissionMapper;
import com.alexandersaul.Alexander.repository.PermissionRepository;
import com.alexandersaul.Alexander.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public List<PermissionResponseDto> findAll() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissionMapper.toListResponseDto(permissions);
    }

    @Override
    public PermissionResponseDto save(PermissionRegisterDto permissionRegisterDto) {
        Permission permission = permissionRepository.save(permissionMapper.toEntity(permissionRegisterDto));
        return permissionMapper.toResponseDto(permission);
    }
}
