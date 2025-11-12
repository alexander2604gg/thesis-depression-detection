package com.alexandersaul.Alexander.dto;

import com.alexandersaul.Alexander.entity.Analysis;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostResponseDto {
    private Long id;
    private String redditId;
    private String author;
    private String title;
    private String content;
    private String recommendation;
    private LocalDateTime createdAtReddit;
    private LocalDateTime fetchedAt;
    private Analysis analysis;
    private boolean isMessageSent;
}
