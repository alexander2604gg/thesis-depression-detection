package com.alexandersaul.Alexander.controller;

import com.alexandersaul.Alexander.dto.rol.RoleRegisterDto;
import com.alexandersaul.Alexander.dto.rol.RoleResponseDto;
import com.alexandersaul.Alexander.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleResponseDto>> findAll () {
        return ResponseEntity.ok(roleService.findAll());
    }

    @PostMapping
    public ResponseEntity<RoleResponseDto> save (@RequestBody RoleRegisterDto roleRegisterDto){
        return ResponseEntity.ok(roleService.save(roleRegisterDto));
    }

}