package com.alexandersaul.Alexander.dto.auth;

import lombok.Data;

@Data
public class AuthLoginDto {
    private String email;
    private String password;
}