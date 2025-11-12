package com.alexandersaul.Alexander.dto.user;

import com.alexandersaul.Alexander.dto.person.PersonRegisterDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterDto {
    private String email;
    private String password;
    private PersonRegisterDto personRegisterDto;
}