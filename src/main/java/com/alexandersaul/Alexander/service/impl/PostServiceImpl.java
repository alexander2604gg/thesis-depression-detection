package com.alexandersaul.Alexander.service.impl;

import com.alexandersaul.Alexander.dto.PostResponseDto;
import com.alexandersaul.Alexander.entity.Lot;
import com.alexandersaul.Alexander.entity.Post;
import com.alexandersaul.Alexander.repository.LotRepository;
import com.alexandersaul.Alexander.repository.PostRepository;
import com.alexandersaul.Alexander.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final LotRepository lotRepository;

    @Override
    public List<Post> getAllPost() {
        return postRepository.findAll();
    }

    @Override
    public List<PostResponseDto> findPostByIdLot(Long idLot){
        Lot lot = lotRepository.findById(idLot).orElseThrow(() -> new RuntimeException("No encontrada"));
        List<Post> posts = postRepository.findAllByLot(lot);
        return posts.stream().map(this::buildResponseDto).toList();
    }

    private PostResponseDto buildResponseDto (Post post){
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .author(post.getAuthor())
                .analysis(post.getAnalysis())
                .content(post.getContent())
                .createdAtReddit(post.getCreatedAtReddit())
                .fetchedAt(post.getFetchedAt())
                .redditId(post.getRedditId())
                .recommendation(post.getRecommendation())
                .isMessageSent(post.isMessageSent())
                .build();
    }
}
