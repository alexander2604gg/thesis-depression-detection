package com.alexandersaul.Alexander.repository;

import com.alexandersaul.Alexander.entity.Role;
import com.alexandersaul.Alexander.entity.UserRole;
import com.alexandersaul.Alexander.entity.UserRoleKey;
import com.alexandersaul.Alexander.entity.UserSec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleKey> {
    List<UserRole> findByUser(UserSec user);
    List<UserRole> findByRole(Role role);
}
