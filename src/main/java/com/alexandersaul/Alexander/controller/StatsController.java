package com.alexandersaul.Alexander.controller;

import com.alexandersaul.Alexander.dto.DepressionStatsDto;
import com.alexandersaul.Alexander.service.impl.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    // GET /api/stats/depression/{forumConfigId}
    @GetMapping("/depression/{forumConfigId}")
    public ResponseEntity<DepressionStatsDto> getDepressionStatsByForumConfig(@PathVariable Long forumConfigId) {
        DepressionStatsDto dto = statsService.getDepressionStatsByForumConfig(forumConfigId);
        return ResponseEntity.ok(dto);
    }
}