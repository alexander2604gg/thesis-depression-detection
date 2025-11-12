package com.alexandersaul.Alexander.repository;

import com.alexandersaul.Alexander.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface AnalysisRepository extends JpaRepository<Analysis, Long> {
    @Query("select avg(a.depressionScore) from Analysis a")
    Double findAverageDepressionScore();

    @Query("select max(a.analyzedAt) from Analysis a")
    LocalDateTime findLastAnalysisDate();
}
