package com.alexandersaul.Alexander.controller;

import com.alexandersaul.Alexander.dto.auth.AuthLoginDto;
import com.alexandersaul.Alexander.dto.auth.AuthResponseDto;
import com.alexandersaul.Alexander.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login (@RequestBody AuthLoginDto authLoginDto){
        return ResponseEntity.ok(customUserDetailsService.login(authLoginDto));
    }

}