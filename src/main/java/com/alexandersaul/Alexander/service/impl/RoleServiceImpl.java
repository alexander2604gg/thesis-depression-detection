package com.alexandersaul.Alexander.service.impl;


import com.alexandersaul.Alexander.dto.rol.RoleRegisterDto;
import com.alexandersaul.Alexander.dto.rol.RoleResponseDto;
import com.alexandersaul.Alexander.entity.Role;
import com.alexandersaul.Alexander.mapper.RoleMapper;
import com.alexandersaul.Alexander.repository.RoleRepository;
import com.alexandersaul.Alexander.service.RoleService;
import com.alexandersaul.Alexander.service.internal.RoleInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService , RoleInternalService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<RoleResponseDto> findAll() {
        List<Role> roleList = roleRepository.findAll();
        return roleMapper.toListResponseDto(roleList);
    }

    @Override
    public RoleResponseDto save(RoleRegisterDto roleRegisterDto) {
        Role role = roleRepository.save(roleMapper.toEntity(roleRegisterDto));
        return roleMapper.toResponseDto(role);
    }

    @Override
    public Role findEntityById(Long roleId) {
        return roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not founded"));
    }
}