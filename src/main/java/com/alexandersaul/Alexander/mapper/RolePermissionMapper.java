package com.alexandersaul.Alexander.mapper;

import com.alexandersaul.Alexander.dto.rolepermission.RolePermissionResponseDto;
import com.alexandersaul.Alexander.entity.RolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RoleMapper.class , PermissionMapper.class})
public interface RolePermissionMapper {

    @Mapping(source = "role" , target = "roleResponseDto")
    @Mapping(source = "permission" , target = "permissionResponseDto")
    RolePermissionResponseDto toResponseDto (RolePermission rolePermission);
    List<RolePermissionResponseDto> toListResponseDto (List<RolePermission> rolePermissionList);
}
