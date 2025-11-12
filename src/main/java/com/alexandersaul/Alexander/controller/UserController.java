package com.alexandersaul.Alexander.controller;

import com.alexandersaul.Alexander.dto.user.UserRegisterDto;
import com.alexandersaul.Alexander.dto.user.UserResponseDto;
import com.alexandersaul.Alexander.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //@PreAuthorize("hasAuthority('READ_USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long userId){
        return ResponseEntity.ok(userService.findById(userId));
    }

    //@PreAuthorize("hasAuthority('CREATE_USER')")
    @PostMapping
    public ResponseEntity<UserResponseDto> save (@RequestBody UserRegisterDto userRegisterDto){
        return ResponseEntity.ok(userService.save(userRegisterDto));
    }

    //@PreAuthorize("hasAuthority('DELETE_USER')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> softDelete (@PathVariable Long userId){
        userService.softDelete(userId);
        return ResponseEntity.noContent().build();
    }

}