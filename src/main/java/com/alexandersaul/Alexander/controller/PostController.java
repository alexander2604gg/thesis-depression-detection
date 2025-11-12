package com.alexandersaul.Alexander.controller;

import com.alexandersaul.Alexander.dto.PostResponseDto;
import com.alexandersaul.Alexander.entity.Post;
import com.alexandersaul.Alexander.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPost();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{idLot}")
    public ResponseEntity<List<PostResponseDto>> getAllPosts(@PathVariable Long idLot) {
        List<PostResponseDto> posts = postService.findPostByIdLot(idLot);
        return ResponseEntity.ok(posts);
    }
}