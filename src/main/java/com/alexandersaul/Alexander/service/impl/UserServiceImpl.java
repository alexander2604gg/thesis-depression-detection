package com.alexandersaul.Alexander.service.impl;

import com.alexandersaul.Alexander.dto.user.UserRegisterDto;
import com.alexandersaul.Alexander.dto.user.UserResponseDto;
import com.alexandersaul.Alexander.entity.Person;
import com.alexandersaul.Alexander.entity.Role;
import com.alexandersaul.Alexander.entity.UserSec;
import com.alexandersaul.Alexander.mapper.UserMapper;
import com.alexandersaul.Alexander.repository.RoleRepository;
import com.alexandersaul.Alexander.repository.UserRepository;
import com.alexandersaul.Alexander.service.UserRoleService;
import com.alexandersaul.Alexander.service.UserService;
import com.alexandersaul.Alexander.service.internal.PersonInternalService;
import com.alexandersaul.Alexander.service.internal.RoleInternalService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PersonInternalService personInternalService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleService userRoleService;

    @Override
    public UserResponseDto save(UserRegisterDto userRegisterDto) {
        Person personSaved = personInternalService.saveEntity(userRegisterDto.getPersonRegisterDto());
        UserSec user = userMapper.toEntity(userRegisterDto);
        user.setPerson(personSaved);
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setEnabled(true);
        UserSec userSaved = userRepository.save(user);
        userRoleService.assignAdminRole(userSaved);
        return userMapper.toResponseDto(userSaved);
    }

    @Override
    public UserResponseDto findById(Long userId) {
        //Aca se debe aplicar excepciones personalizadas
        UserSec userSec = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not founded"));
        return userMapper.toResponseDto(userSec);
    }

    @Override
    public void softDelete(Long userId) {
        //Aca se debe aplicar excepciones personalizadas
        UserSec userSec = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not founded"));
        userSec.setEnabled(false);
        userRepository.save(userSec);
    }

}
