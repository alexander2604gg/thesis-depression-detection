package com.alexandersaul.Alexander.service;

import com.alexandersaul.Alexander.dto.rolepermission.RolePermissionRegisterDto;
import com.alexandersaul.Alexander.dto.rolepermission.RolePermissionResponseDto;

import java.util.List;

public interface RolePermissionService {
    List<RolePermissionResponseDto> findAll ();
    RolePermissionResponseDto save (RolePermissionRegisterDto rolePermissionRegisterDto);
}
