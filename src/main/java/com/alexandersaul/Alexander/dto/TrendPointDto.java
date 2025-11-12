package com.alexandersaul.Alexander.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TrendPointDto {
    private String date;
    private double avgScore;

    public TrendPointDto(Integer year, Integer month, Integer day, Double avgScore) {
        LocalDate d = LocalDate.of(year, month, day);
        this.date = d.toString();
        this.avgScore = avgScore != null ? avgScore : 0.0;
    }
}