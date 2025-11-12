package com.alexandersaul.Alexander.repository;

import com.alexandersaul.Alexander.entity.UserSec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserSec,Long> {
    Optional<UserSec> findByEmail (String email);
}
