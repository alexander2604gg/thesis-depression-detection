package com.alexandersaul.Alexander.repository;

import com.alexandersaul.Alexander.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission,Long> {
}
