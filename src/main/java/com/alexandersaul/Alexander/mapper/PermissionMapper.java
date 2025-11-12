package com.alexandersaul.Alexander.mapper;

import com.alexandersaul.Alexander.dto.permission.PermissionRegisterDto;
import com.alexandersaul.Alexander.dto.permission.PermissionResponseDto;
import com.alexandersaul.Alexander.entity.Permission;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toEntity (PermissionRegisterDto permissionRegisterDto);
    PermissionResponseDto toResponseDto (Permission permission);
    List<PermissionResponseDto> toListResponseDto (List<Permission> permissionList);
}