package com.alexandersaul.Alexander.controller;

import com.alexandersaul.Alexander.dto.permission.PermissionRegisterDto;
import com.alexandersaul.Alexander.dto.permission.PermissionResponseDto;
import com.alexandersaul.Alexander.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<PermissionResponseDto>> findAll () {
        return ResponseEntity.ok(permissionService.findAll());
    }

    @PostMapping
    public ResponseEntity<PermissionResponseDto> save (@RequestBody PermissionRegisterDto permissionRegisterDto) {
        return ResponseEntity.ok(permissionService.save(permissionRegisterDto));
    }

}