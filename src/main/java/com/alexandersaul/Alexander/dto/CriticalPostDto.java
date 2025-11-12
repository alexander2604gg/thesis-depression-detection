package com.alexandersaul.Alexander.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CriticalPostDto {
    private Long postId;
    private String subreddit;
    private Double depressionScore;
    private String label;
    private LocalDateTime date;
    private String excerpt;
    private String author;
}