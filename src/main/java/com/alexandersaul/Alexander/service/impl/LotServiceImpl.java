package com.alexandersaul.Alexander.service.impl;

import com.alexandersaul.Alexander.dto.LotResponseDto;
import com.alexandersaul.Alexander.entity.ForumConfig;
import com.alexandersaul.Alexander.entity.Lot;
import com.alexandersaul.Alexander.entity.Post;
import com.alexandersaul.Alexander.repository.ForumConfigRepository;
import com.alexandersaul.Alexander.repository.LotRepository;
import com.alexandersaul.Alexander.service.LotService;
import com.alexandersaul.Alexander.service.RedditService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LotServiceImpl implements LotService {

    private final ForumConfigRepository forumConfigRepository;
    private final LotRepository lotRepository;
    private final RedditService redditService;

    @Scheduled(fixedRate = 60_000)
    @Override
    public void createLotsIfNeeded() {
        log.info("Scheduler ejecutado: {}", LocalDateTime.now());
        LocalDateTime now = LocalDateTime.now();

        List<Lot> lotsToCreate = forumConfigRepository.findByIsActiveTrue().stream()
                .filter(config -> isWithinDateRange(config, now))
                .filter(config -> shouldCreateLot(config, now))
                .map(config -> {
                    Lot lot = new Lot();
                    lot.setForumConfig(config);
                    lot.setCreatedAt(now);
                    return lot;
                })
                .toList();

        if (!lotsToCreate.isEmpty()) {
            List<Lot> savedLots = lotRepository.saveAll(lotsToCreate);
            redditService.obtainPosts(savedLots);
        }
    }


    private boolean isWithinDateRange(ForumConfig config, LocalDateTime now) {
        return (now.isAfter(config.getStartDate()) || now.isEqual(config.getStartDate()))
                && (now.isBefore(config.getEndDate()) || now.isEqual(config.getEndDate()));
    }

    private boolean shouldCreateLot(ForumConfig config, LocalDateTime now) {
        return lotRepository.findTopByForumConfigOrderByCreatedAtDesc(config)
                .map(lastLot -> {
                    LocalDateTime nextExecutionTime = lastLot.getCreatedAt()
                            .plusMinutes((long) config.getInterval());
                    return now.isAfter(nextExecutionTime);
                })
                .orElse(true);
    }

    @Override
    public List<LotResponseDto> findLotsByConfig (Long idConfig){
        ForumConfig forumConfig = forumConfigRepository.findById(idConfig).orElseThrow(() -> new RuntimeException("No encontrada"));
        List<Lot> lots = lotRepository.findByForumConfig(forumConfig);
        return lots.stream().map(this::buildResponseDto).toList();
    }

    private LotResponseDto buildResponseDto (Lot lot) {
        List<Post> postsWithDepression = lot.getPosts().stream()
                .filter(post -> post.getAnalysis() != null
                        && post.getAnalysis().getDepressionScore() != null
                        && "LABEL_1".equals( post.getAnalysis().getLabel()))
                .toList();
        return LotResponseDto.builder()
                .id(lot.getId())
                .createdAt(lot.getCreatedAt())
                .sizePosts(lot.getPosts().size())
                .sizePostsDepression(postsWithDepression.size())
                .build();
    }

}
