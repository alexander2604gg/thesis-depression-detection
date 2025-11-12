package com.alexandersaul.Alexander.service.impl;

import com.alexandersaul.Alexander.client.RedditClient;
import com.alexandersaul.Alexander.dto.RedditResponseDto;
import com.alexandersaul.Alexander.entity.Lot;
import com.alexandersaul.Alexander.entity.Post;
import com.alexandersaul.Alexander.repository.PostRepository;
import com.alexandersaul.Alexander.service.AnalysisService;
import com.alexandersaul.Alexander.service.RedditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedditServiceImpl implements RedditService {

    private final RedditClient redditClient;
    private final PostRepository postRepository;
    private final AnalysisService analysisService;

    @Override
    public void obtainPosts(List<Lot> lotList) {
        List<Post> posts = lotList.stream()
                .flatMap(lot -> {
                    String subReddit = lot.getForumConfig().getSubreddit();
                    RedditResponseDto response = redditClient.getNewPosts(subReddit, 4);

                    return response.getData().getChildren().stream()
                            .map(child -> {
                                RedditResponseDto.RedditPost redditPost = child.getData();
                                Post post = new Post();
                                post.setLot(lot);
                                post.setRedditId(redditPost.getId());
                                post.setAuthor(redditPost.getAuthor());
                                post.setTitle(redditPost.getTitle());
                                post.setContent(redditPost.getSelftext());
                                post.setCreatedAtReddit(LocalDateTime.ofInstant(
                                        Instant.ofEpochSecond(redditPost.getCreated_utc()), ZoneOffset.UTC)
                                );
                                return post;
                            });
                })
                .toList();

        // 🔑 Consulta todos los IDs ya existentes
        List<String> redditIds = posts.stream()
                .map(Post::getRedditId)
                .toList();

        List<String> existingIds = postRepository.findAllByRedditIdIn(redditIds)
                .stream()
                .map(Post::getRedditId)
                .toList();

        List<Post> newPosts = posts.stream()
                .filter(p -> !existingIds.contains(p.getRedditId()))
                .toList();

        if (newPosts.isEmpty()) {
            log.info("No hay posts nuevos para guardar");
            return;
        }

        List<Post> postsSaved = postRepository.saveAll(newPosts);
        analysisService.analyzePosts(postsSaved);
    }


}
