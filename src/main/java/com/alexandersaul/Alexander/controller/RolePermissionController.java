package com.alexandersaul.Alexander.controller;

import com.alexandersaul.Alexander.dto.rolepermission.RolePermissionRegisterDto;
import com.alexandersaul.Alexander.dto.rolepermission.RolePermissionResponseDto;
import com.alexandersaul.Alexander.service.RolePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role-permission")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @GetMapping
    private ResponseEntity<List<RolePermissionResponseDto>> findAll () {
        return ResponseEntity.ok(rolePermissionService.findAll());
    }

    @PostMapping
    private ResponseEntity<RolePermissionResponseDto> save (@RequestBody RolePermissionRegisterDto rolePermissionRegisterDto) {
        return ResponseEntity.ok(rolePermissionService.save(rolePermissionRegisterDto));
    }

}