package com.alexandersaul.Alexander.service;

import com.alexandersaul.Alexander.dto.user.UserRegisterDto;
import com.alexandersaul.Alexander.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto save (UserRegisterDto userRegisterDto);
    UserResponseDto findById (Long userId);
    void softDelete (Long userId);
}