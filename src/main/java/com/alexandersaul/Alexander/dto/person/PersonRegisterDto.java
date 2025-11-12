package com.alexandersaul.Alexander.dto.person;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonRegisterDto {
    private String firstName;
    private String secondName;
    private String country;
    private String phoneNumber;
}