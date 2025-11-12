package com.alexandersaul.Alexander.mapper;

import com.alexandersaul.Alexander.dto.user.UserRegisterDto;
import com.alexandersaul.Alexander.dto.user.UserResponseDto;
import com.alexandersaul.Alexander.entity.UserSec;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface UserMapper {
    UserSec toEntity (UserRegisterDto userRegisterDto);
    @Mapping(source = "person" , target = "personResponseDto")
    UserResponseDto toResponseDto (UserSec userSec);
}