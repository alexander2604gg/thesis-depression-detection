package com.alexandersaul.Alexander.service;

import com.alexandersaul.Alexander.dto.LotResponseDto;

import java.util.List;

public interface LotService {
    void createLotsIfNeeded();
    List<LotResponseDto> findLotsByConfig (Long idConfig);
}
