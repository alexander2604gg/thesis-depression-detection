package com.alexandersaul.Alexander.repository;

import com.alexandersaul.Alexander.entity.ForumConfig;
import com.alexandersaul.Alexander.entity.Lot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LotRepository extends JpaRepository<Lot,Long> {
    Optional<Lot> findTopByForumConfigOrderByCreatedAtDesc(ForumConfig config);
    List<Lot> findByForumConfig(ForumConfig forumConfig);
}
