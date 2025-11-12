package com.alexandersaul.Alexander.mapper;

import com.alexandersaul.Alexander.dto.rol.RoleRegisterDto;
import com.alexandersaul.Alexander.dto.rol.RoleResponseDto;
import com.alexandersaul.Alexander.entity.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toEntity (RoleRegisterDto roleRegisterDto);
    RoleResponseDto toResponseDto (Role role);
    List<RoleResponseDto> toListResponseDto (List<Role> roleList);
}