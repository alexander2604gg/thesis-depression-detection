package com.alexandersaul.Alexander.service.impl;

import com.alexandersaul.Alexander.dto.DayStatDto;
import com.alexandersaul.Alexander.dto.DepressionStatsDto;
import com.alexandersaul.Alexander.entity.Post;
import com.alexandersaul.Alexander.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final PostRepository postRepository;

    private static final String DEPRESSION_LABEL = "LABEL_1";

    public DepressionStatsDto getDepressionStatsByForumConfig(Long forumConfigId) {
        long totalPosts = postRepository.countByLot_ForumConfig_Id(forumConfigId);
        long depressivePosts = postRepository.countByLot_ForumConfig_IdAndAnalysis_Label(forumConfigId, DEPRESSION_LABEL);

        double percentage = totalPosts == 0 ? 0.0 : ((double) depressivePosts * 100.0) / totalPosts;
        percentage = BigDecimal.valueOf(percentage).setScale(2, RoundingMode.HALF_UP).doubleValue();

        // Traemos solo los posts con label = LABEL_1 para agrupar por día
        List<Post> depressiveList = postRepository.findByLot_ForumConfig_IdAndAnalysis_Label(forumConfigId, DEPRESSION_LABEL);

        Map<DayOfWeek, Long> grouped = depressiveList.stream()
                .filter(p -> p.getCreatedAtReddit() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getCreatedAtReddit().getDayOfWeek(),
                        Collectors.counting()
                ));

        List<DayStatDto> weeklyStats = Arrays.stream(DayOfWeek.values())
                .map(day -> DayStatDto.builder()
                        .dayOfWeek(toSpanish(day))
                        .depressivePosts(grouped.getOrDefault(day, 0L))
                        .build())
                .toList();

        return DepressionStatsDto.builder()
                .totalPosts(totalPosts)
                .totalDepressivePosts(depressivePosts)
                .percentageDepressive(percentage)
                .weeklyStats(weeklyStats)
                .build();
    }

    private String toSpanish(DayOfWeek day) {
        return switch (day) {
            case MONDAY -> "Lunes";
            case TUESDAY -> "Martes";
            case WEDNESDAY -> "Miércoles";
            case THURSDAY -> "Jueves";
            case FRIDAY -> "Viernes";
            case SATURDAY -> "Sábado";
            case SUNDAY -> "Domingo";
        };
    }
}
