package com.alexandersaul.Alexander.repository;

import com.alexandersaul.Alexander.entity.Role;
import com.alexandersaul.Alexander.entity.RolePermission;
import com.alexandersaul.Alexander.entity.RolePermissionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionKey> {
    List<RolePermission> findByRoleIn(List<Role> roles);
}
