package com.alexandersaul.Alexander.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ForumConfigResponseDto {
    private Long id;
    private String subreddit;
    private boolean isActive;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private float interval;
}
