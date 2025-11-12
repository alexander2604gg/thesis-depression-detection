package com.alexandersaul.Alexander.service;

import com.alexandersaul.Alexander.dto.ForumConfigCreateDto;
import com.alexandersaul.Alexander.dto.ForumConfigResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ForumConfigService {
    void create (ForumConfigCreateDto forumConfigCreateDto) ;
    List<ForumConfigResponseDto> findAll ();
    void deleteById (Long id);
    List<ForumConfigResponseDto> findByStartDate(LocalDateTime startDate);
    ForumConfigResponseDto findById(Long id);
}
