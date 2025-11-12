package com.alexandersaul.Alexander.controller;

import com.alexandersaul.Alexander.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @PostMapping("/ask")
    public ResponseEntity<String> getRecommendation (Long postId){
        return ResponseEntity.ok(recommendationService.askRecommendation(postId));
    }
}
