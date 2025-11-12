package com.alexandersaul.Alexander.service;

import com.alexandersaul.Alexander.dto.rol.RoleRegisterDto;
import com.alexandersaul.Alexander.dto.rol.RoleResponseDto;

import java.util.List;

public interface RoleService {
    List<RoleResponseDto> findAll ();
    RoleResponseDto save (RoleRegisterDto roleRegisterDto);
}
