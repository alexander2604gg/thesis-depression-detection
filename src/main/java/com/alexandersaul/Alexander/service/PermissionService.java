package com.alexandersaul.Alexander.service;

import com.alexandersaul.Alexander.dto.permission.PermissionRegisterDto;
import com.alexandersaul.Alexander.dto.permission.PermissionResponseDto;

import java.util.List;

public interface PermissionService {
    List<PermissionResponseDto> findAll ();
    PermissionResponseDto save (PermissionRegisterDto permissionRegisterDto);
}