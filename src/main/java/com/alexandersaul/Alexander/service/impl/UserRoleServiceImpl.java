package com.alexandersaul.Alexander.service.impl;

import com.alexandersaul.Alexander.entity.Role;
import com.alexandersaul.Alexander.entity.UserRole;
import com.alexandersaul.Alexander.entity.UserRoleKey;
import com.alexandersaul.Alexander.entity.UserSec;
import com.alexandersaul.Alexander.repository.RoleRepository;
import com.alexandersaul.Alexander.repository.UserRoleRepository;
import com.alexandersaul.Alexander.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    @Override
    public void assignAdminRole(UserSec userSec) {
        Role role = roleRepository.findRoleByName("ADMIN").orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        userRoleRepository.save(buildUserRole(userSec,role));
    }

    public UserRole buildUserRole (UserSec userSec , Role role){
        return UserRole.builder()
                .userRoleKey(buildUserRoleKey(userSec,role))
                .role(role)
                .user(userSec)
                .build();
    }

    public UserRoleKey buildUserRoleKey (UserSec userSec , Role role) {
        return UserRoleKey.builder()
                .roleId(role.getRoleId())
                .userId(userSec.getUserId())
                .build();
    }

}
