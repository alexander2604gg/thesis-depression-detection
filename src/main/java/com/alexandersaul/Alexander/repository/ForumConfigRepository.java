package com.alexandersaul.Alexander.repository;

import com.alexandersaul.Alexander.entity.ForumConfig;
import com.alexandersaul.Alexander.entity.UserSec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ForumConfigRepository extends JpaRepository<ForumConfig, Long> {

    // 🔹 Tus métodos originales
    List<ForumConfig> findByIsActiveTrue();
    long countByIsActiveTrue();
    List<ForumConfig> findByStartDate(LocalDateTime startDate);

    // 🔹 Nuevos métodos por usuario autenticado
    List<ForumConfig> findByUser(UserSec user);
    Optional<ForumConfig> findByIdAndUser(Long id, UserSec user);
    List<ForumConfig> findByUserAndStartDate(UserSec user, LocalDateTime startDate);
}
