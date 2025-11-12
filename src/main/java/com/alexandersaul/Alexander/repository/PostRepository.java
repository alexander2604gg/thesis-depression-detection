package com.alexandersaul.Alexander.repository;

import com.alexandersaul.Alexander.entity.Lot;
import com.alexandersaul.Alexander.entity.Post;
import com.alexandersaul.Alexander.dto.SubredditAverageProjection;
import com.alexandersaul.Alexander.dto.TrendPointDto;
import com.alexandersaul.Alexander.dto.RangeCountProjection;
import com.alexandersaul.Alexander.dto.CriticalPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByRedditIdIn(List<String> redditIds);
    List<Post> findAllByLot(Lot lot);

    // total de posts del forumConfig
    long countByLot_ForumConfig_Id(Long forumConfigId);

    // total de posts del forumConfig con analysis.label = "LABEL_1"
    long countByLot_ForumConfig_IdAndAnalysis_Label(Long forumConfigId, String label);

    // trae solo los posts que cumplen analysis.label = "LABEL_1" (para agrupar por día)
    List<Post> findByLot_ForumConfig_IdAndAnalysis_Label(Long forumConfigId, String label);

    // promedio de score por subreddit (top N ordenado desc)
    @Query("select fc.subreddit as subreddit, avg(a.depressionScore) as avgScore " +
            "from Post p join p.analysis a join p.lot l join l.forumConfig fc " +
            "group by fc.subreddit order by avgScore desc")
    List<SubredditAverageProjection> findSubredditAverage(Pageable pageable);

    @Query("select new com.alexandersaul.Alexander.dto.TrendPointDto(year(a.analyzedAt), month(a.analyzedAt), day(a.analyzedAt), avg(a.depressionScore)) " +
            "from Post p join p.analysis a join p.lot l join l.forumConfig fc " +
            "where a.depressionScore is not null " +
            "and fc.subreddit = coalesce(:subreddit, fc.subreddit) " +
            "and a.analyzedAt >= coalesce(:start, a.analyzedAt) " +
            "and a.analyzedAt <= coalesce(:end, a.analyzedAt) " +
            "group by year(a.analyzedAt), month(a.analyzedAt), day(a.analyzedAt) " +
            "order by year(a.analyzedAt), month(a.analyzedAt), day(a.analyzedAt)")
    List<TrendPointDto> findTrend(String subreddit, java.time.LocalDateTime start, java.time.LocalDateTime end);

    @Query("select " +
            "case " +
            " when a.depressionScore >= 0.0 and a.depressionScore < 0.2 then '0.0 - 0.2' " +
            " when a.depressionScore >= 0.2 and a.depressionScore < 0.4 then '0.2 - 0.4' " +
            " when a.depressionScore >= 0.4 and a.depressionScore < 0.6 then '0.4 - 0.6' " +
            " when a.depressionScore >= 0.6 and a.depressionScore < 0.8 then '0.6 - 0.8' " +
            " else '0.8 - 1.0' end as range, " +
            "count(p) as cnt " +
            "from Post p join p.analysis a join p.lot l join l.forumConfig fc " +
            "where a.depressionScore is not null and (:subreddit is null or fc.subreddit = :subreddit) " +
            "group by case " +
            " when a.depressionScore >= 0.0 and a.depressionScore < 0.2 then '0.0 - 0.2' " +
            " when a.depressionScore >= 0.2 and a.depressionScore < 0.4 then '0.2 - 0.4' " +
            " when a.depressionScore >= 0.4 and a.depressionScore < 0.6 then '0.4 - 0.6' " +
            " when a.depressionScore >= 0.6 and a.depressionScore < 0.8 then '0.6 - 0.8' " +
            " else '0.8 - 1.0' end")
    List<RangeCountProjection> findScoreDistribution(String subreddit);

    @Query("select new com.alexandersaul.Alexander.dto.CriticalPostDto(" +
            " p.id, fc.subreddit, a.depressionScore, " +
            " case when a.label = 'LABEL_1' then 'Depressed' else 'NotDepressed' end, " +
            " a.analyzedAt, " +
            " coalesce(p.content, ''), " + // 👈 mostrar todo el contenido completo
            " p.author) " +
            "from Post p join p.analysis a join p.lot l join l.forumConfig fc " +
            "where a.depressionScore is not null and a.depressionScore > :threshold " +
            "order by a.depressionScore desc, a.analyzedAt desc")
    List<CriticalPostDto> findCriticalPosts(@Param("threshold") double threshold, Pageable pageable);


    @Query("select new com.alexandersaul.Alexander.dto.CriticalPostDto(" +
            " p.id, fc.subreddit, a.depressionScore, " +
            " case when a.label = 'LABEL_1' then 'Depressed' else 'NotDepressed' end, " +
            " a.analyzedAt, " +
            " coalesce(p.content, ''), " +
            " p.author) " +
            "from Post p join p.analysis a join p.lot l join l.forumConfig fc " +
            "where a.depressionScore is not null " +
            "order by a.depressionScore desc, a.analyzedAt desc")
    Page<CriticalPostDto> findAllAnalyzedPosts(Pageable pageable);


}