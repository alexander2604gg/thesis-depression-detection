package com.alexandersaul.Alexander.mapper;

import com.alexandersaul.Alexander.dto.person.PersonRegisterDto;
import com.alexandersaul.Alexander.dto.person.PersonResponseDto;
import com.alexandersaul.Alexander.entity.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    Person toEntity (PersonRegisterDto userRegisterDto);
    PersonResponseDto toResponseDto (Person person);
}