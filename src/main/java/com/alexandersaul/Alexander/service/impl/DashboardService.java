package com.alexandersaul.Alexander.service.impl;

import com.alexandersaul.Alexander.dto.*;
import com.alexandersaul.Alexander.entity.Post;
import com.alexandersaul.Alexander.repository.AnalysisRepository;
import com.alexandersaul.Alexander.repository.ForumConfigRepository;
import com.alexandersaul.Alexander.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PostRepository postRepository;
    private final AnalysisRepository analysisRepository;
    private final ForumConfigRepository forumConfigRepository;

    public Page<CriticalPostDto> getAllAnalyzedPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findAllAnalyzedPosts(pageable);
    }


    public DashboardOverviewDto getOverview() {
        long totalPosts = postRepository.count();

        Double averageScore = analysisRepository.findAverageDepressionScore();
        double avg = averageScore != null ? averageScore : 0.0;

        List<SubredditAverageProjection> topAvg = postRepository.findSubredditAverage(PageRequest.of(0, 1));
        String mostDepressedSubreddit = topAvg.isEmpty() ? null : topAvg.get(0).getSubreddit();

        long totalSubredditsActive = forumConfigRepository.countByIsActiveTrue();

        LocalDateTime lastAnalysisDate = analysisRepository.findLastAnalysisDate();

        return DashboardOverviewDto.builder()
                .totalPosts(totalPosts)
                .averageDepressionScore(avg)
                .mostDepressedSubreddit(mostDepressedSubreddit)
                .totalSubredditsActive(totalSubredditsActive)
                .lastAnalysisDate(lastAnalysisDate)
                .build();
    }

    public List<TrendPointDto> getTrend(String subreddit, LocalDateTime start, LocalDateTime end) {
        return postRepository.findTrend(subreddit, start, end);
    }

    public List<DistributionBucketDto> getDistribution(String subreddit) {
        List<String> ranges = List.of(
                "0.0 - 0.2",
                "0.2 - 0.4",
                "0.4 - 0.6",
                "0.6 - 0.8",
                "0.8 - 1.0"
        );

        List<RangeCountProjection> results = postRepository.findScoreDistribution(subreddit);
        Map<String, Long> counts = results.stream()
                .collect(Collectors.toMap(RangeCountProjection::getRange, RangeCountProjection::getCnt));

        return ranges.stream()
                .map(r -> DistributionBucketDto.builder()
                        .range(r)
                        .count(counts.getOrDefault(r, 0L))
                        .build())
                .toList();
    }

    public List<CriticalPostDto> getCriticalPosts(Double threshold, Integer limit) {
        double th = threshold != null ? threshold : 0.8d;
        int lim = (limit != null && limit > 0) ? limit : 20;
        return postRepository.findCriticalPosts(th, PageRequest.of(0, lim));
    }
}