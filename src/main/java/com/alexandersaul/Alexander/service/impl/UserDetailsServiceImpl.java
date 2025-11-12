package com.alexandersaul.Alexander.service.impl;

import com.alexandersaul.Alexander.dto.auth.AuthLoginDto;
import com.alexandersaul.Alexander.dto.auth.AuthResponseDto;
import com.alexandersaul.Alexander.entity.*;
import com.alexandersaul.Alexander.repository.RolePermissionRepository;
import com.alexandersaul.Alexander.repository.UserRepository;
import com.alexandersaul.Alexander.repository.UserRoleRepository;
import com.alexandersaul.Alexander.service.CustomUserDetailsService;
import com.alexandersaul.Alexander.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserSec userSec = userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User was not found"));

        Set<GrantedAuthority> authorityList = new HashSet<>();

        List<UserRole> userRoles = userRoleRepository.findByUser(userSec);
        List<Role> roles = userRoles.stream()
                .map(UserRole::getRole)
                .toList();
        roles.forEach(rol -> fillAuthorityListWithRoles(authorityList,rol));

        List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleIn(roles);
        List<Permission> permissions = rolePermissions.stream()
                .map(RolePermission::getPermission)
                .toList();

        permissions.forEach(permission -> fillAuthorityListWithPermissions(authorityList,permission));

        return new User(userSec.getEmail() , userSec.getPassword() , userSec.getEnabled() , true,
                true , true , authorityList);

    }

    public AuthResponseDto login (AuthLoginDto authLoginDto) {
        Authentication authentication = authenticate(authLoginDto);
        putAuthenticationInContextHolder(authentication);
        String jwt = jwtUtil.createToken(authentication);
        return AuthResponseDto.builder()
                .email(authLoginDto.getEmail())
                .jwt(jwt)
                .build();
    }

    private Authentication authenticate (AuthLoginDto authLoginDto){
        UserDetails userDetails = this.loadUserByUsername(authLoginDto.getEmail());

        if (!passwordEncoder.matches(authLoginDto.getPassword(),userDetails.getPassword())) {
            throw new BadCredentialsException("invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(authLoginDto.getEmail(),
                null,userDetails.getAuthorities());
    }

    private void fillAuthorityListWithRoles (Set<GrantedAuthority> authorities, Role role) {
        authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getName())));
    }

    private void fillAuthorityListWithPermissions (Set<GrantedAuthority> authorities, Permission permission) {
        authorities.add(new SimpleGrantedAuthority(permission.getName()));
    }

    private void putAuthenticationInContextHolder (Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}