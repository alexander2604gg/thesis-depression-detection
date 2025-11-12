package com.alexandersaul.Alexander.service.impl;

import com.alexandersaul.Alexander.client.RedditClient;
import com.alexandersaul.Alexander.dto.SubredditAboutDto;
import com.alexandersaul.Alexander.dto.ForumConfigCreateDto;
import com.alexandersaul.Alexander.dto.ForumConfigResponseDto;
import com.alexandersaul.Alexander.entity.ForumConfig;
import com.alexandersaul.Alexander.entity.UserSec;
import com.alexandersaul.Alexander.repository.ForumConfigRepository;
import com.alexandersaul.Alexander.repository.UserRepository;
import com.alexandersaul.Alexander.service.ForumConfigService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ForumConfigServiceImpl implements ForumConfigService {


    private final ForumConfigRepository forumConfigRepository;
    private final UserRepository userRepository;
    private final RedditClient redditClient;

    @Override
    public void create(ForumConfigCreateDto forumConfigCreateDto) {
        UserSec user = getAuthenticatedUser();

        validateSubredditExists(forumConfigCreateDto.getSubreddit());

        ForumConfig forumConfig = buildForumConfig(forumConfigCreateDto);
        forumConfig.setUser(user);

        forumConfigRepository.save(forumConfig);
    }

    @Override
    public List<ForumConfigResponseDto> findAll() {
        UserSec user = getAuthenticatedUser();

        List<ForumConfig> forumConfigs = forumConfigRepository.findByUser(user);
        return forumConfigs.stream()
                .map(this::buildForumConfigResponse)
                .toList();
    }

    @Override
    public ForumConfigResponseDto findById(Long id) {
        UserSec user = getAuthenticatedUser();

        ForumConfig forumConfig = forumConfigRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("ForumConfig no encontrada o no pertenece al usuario"));

        return buildForumConfigResponse(forumConfig);
    }

    @Override
    public List<ForumConfigResponseDto> findByStartDate(LocalDateTime startDate) {
        UserSec user = getAuthenticatedUser();

        List<ForumConfig> forumConfigs = forumConfigRepository.findByUserAndStartDate(user, startDate);
        return forumConfigs.stream()
                .map(this::buildForumConfigResponse)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        UserSec user = getAuthenticatedUser();

        ForumConfig forumConfig = forumConfigRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("No se encontró el recurso o no pertenece al usuario"));

        forumConfigRepository.delete(forumConfig);
    }

    // ====================================================
    // Métodos auxiliares
    // ====================================================

    private ForumConfig buildForumConfig(ForumConfigCreateDto dto) {
        return ForumConfig.builder()
                .subreddit(dto.getSubreddit())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .interval(dto.getInterval())
                .isActive(true)
                .build();
    }

    private void validateSubredditExists(String subreddit) {
        try {
            SubredditAboutDto about = redditClient.getSubredditAbout(subreddit);
            if (about == null || about.getData() == null ||
                    about.getData().getDisplay_name() == null || about.getData().getDisplay_name().isBlank()) {
                throw new RuntimeException("El subreddit no existe o no es accesible");
            }
        } catch (Exception e) {
            throw new RuntimeException("El subreddit no existe o no es accesible");
        }
    }

    private ForumConfigResponseDto buildForumConfigResponse(ForumConfig entity) {
        return ForumConfigResponseDto.builder()
                .id(entity.getId())
                .subreddit(entity.getSubreddit())
                .isActive(entity.isActive())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .interval(entity.getInterval())
                .build();
    }

    /**
     * Obtiene el usuario autenticado desde el SecurityContext.
     */
    private UserSec getAuthenticatedUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));
    }

}
