package com.alexandersaul.Alexander.dto.rolepermission;

import com.alexandersaul.Alexander.dto.permission.PermissionResponseDto;
import com.alexandersaul.Alexander.dto.rol.RoleResponseDto;
import lombok.Data;

@Data
public class RolePermissionResponseDto {
    RoleResponseDto roleResponseDto;
    PermissionResponseDto permissionResponseDto;
}
