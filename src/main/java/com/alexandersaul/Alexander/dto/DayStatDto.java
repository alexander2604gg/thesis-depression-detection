package com.alexandersaul.Alexander.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DayStatDto {
    private String dayOfWeek;
    private long depressivePosts;
}