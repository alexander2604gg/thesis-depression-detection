package com.alexandersaul.Alexander.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardOverviewDto {
    private long totalPosts;
    private double averageDepressionScore;
    private String mostDepressedSubreddit;
    private long totalSubredditsActive;
    private LocalDateTime lastAnalysisDate;
}