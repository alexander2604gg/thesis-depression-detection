package com.alexandersaul.Alexander.controller;

import com.alexandersaul.Alexander.dto.LotResponseDto;
import com.alexandersaul.Alexander.service.LotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lot")
@RequiredArgsConstructor
public class LotController {
    private final LotService lotService;

    @GetMapping("/by-config/{idConfig}")
    public ResponseEntity<List<LotResponseDto>> findLotsByConfig(@PathVariable Long idConfig) {
        List<LotResponseDto> lots = lotService.findLotsByConfig(idConfig);
        return ResponseEntity.ok(lots);
    }
}
