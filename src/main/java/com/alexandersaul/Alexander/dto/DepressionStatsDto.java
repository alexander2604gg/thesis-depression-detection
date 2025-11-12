package com.alexandersaul.Alexander.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepressionStatsDto {
    private long totalPosts;
    private long totalDepressivePosts;
    private double percentageDepressive;
    private List<DayStatDto> weeklyStats;
}
