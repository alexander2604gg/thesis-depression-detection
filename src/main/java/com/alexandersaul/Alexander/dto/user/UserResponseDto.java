package com.alexandersaul.Alexander.dto.user;

import com.alexandersaul.Alexander.dto.person.PersonResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
    private Long userId;
    private String email;
    private PersonResponseDto personResponseDto;
}