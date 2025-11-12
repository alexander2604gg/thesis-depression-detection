package com.alexandersaul.Alexander.service;

import com.alexandersaul.Alexander.dto.auth.AuthLoginDto;
import com.alexandersaul.Alexander.dto.auth.AuthResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomUserDetailsService extends UserDetailsService {
    AuthResponseDto login(AuthLoginDto authLoginDto);
}
