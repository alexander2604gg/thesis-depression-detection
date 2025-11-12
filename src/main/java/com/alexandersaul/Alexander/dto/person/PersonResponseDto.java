package com.alexandersaul.Alexander.dto.person;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonResponseDto {
    private Long personId;
    private String firstName;
    private String secondName;
}