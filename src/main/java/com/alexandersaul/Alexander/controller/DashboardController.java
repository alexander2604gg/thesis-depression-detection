package com.alexandersaul.Alexander.controller;

import com.alexandersaul.Alexander.dto.*;
import com.alexandersaul.Alexander.service.impl.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/posts/all")
    public ResponseEntity<Page<CriticalPostDto>> getAllAnalyzedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(dashboardService.getAllAnalyzedPosts(page, size));
    }

    @GetMapping("/overview")
    public ResponseEntity<DashboardOverviewDto> getOverview() {
        DashboardOverviewDto dto = dashboardService.getOverview();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/trend")
    public ResponseEntity<List<TrendPointDto>> getTrend(
            @RequestParam(value = "subreddit", required = false) String subreddit,
            @RequestParam(value = "start", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime start,
            @RequestParam(value = "end", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime end
    ) {
        List<TrendPointDto> trend = dashboardService.getTrend(subreddit, start, end);
        return ResponseEntity.ok(trend);
    }

    @GetMapping("/distribution")
    public ResponseEntity<List<DistributionBucketDto>> getDistribution(
            @RequestParam(value = "subreddit", required = false) String subreddit
    ) {
        List<DistributionBucketDto> distribution = dashboardService.getDistribution(subreddit);
        return ResponseEntity.ok(distribution);
    }

    @GetMapping("/posts/critical")
    public ResponseEntity<List<CriticalPostDto>> getCriticalPosts(
            @RequestParam(value = "threshold", required = false, defaultValue = "0.8") Double threshold,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit
    ) {
        List<CriticalPostDto> posts = dashboardService.getCriticalPosts(threshold, limit);
        return ResponseEntity.ok(posts);
    }
}