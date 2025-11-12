package com.alexandersaul.Alexander.service;

import com.alexandersaul.Alexander.dto.person.PersonRegisterDto;
import com.alexandersaul.Alexander.dto.person.PersonResponseDto;

public interface PersonService{
    PersonResponseDto save (PersonRegisterDto personRegisterDto);
    void delete (Long personId);
}