package com.alexandersaul.Alexander.service.internal;

import com.alexandersaul.Alexander.dto.person.PersonRegisterDto;
import com.alexandersaul.Alexander.entity.Person;

public interface PersonInternalService {
    Person saveEntity (PersonRegisterDto personRegisterDto);
}