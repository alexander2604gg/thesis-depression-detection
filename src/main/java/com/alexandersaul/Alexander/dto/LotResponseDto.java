package com.alexandersaul.Alexander.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class LotResponseDto {
    private Long id;
    private LocalDateTime createdAt;
    private int sizePosts;
    private int sizePostsDepression;
}
