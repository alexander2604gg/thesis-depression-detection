package com.alexandersaul.Alexander.controller;

import com.alexandersaul.Alexander.dto.SendRecommendationRequestDto;
import com.alexandersaul.Alexander.entity.Post;
import com.alexandersaul.Alexander.repository.PostRepository;
import com.alexandersaul.Alexander.service.RedditMessageService;
import com.alexandersaul.Alexander.service.RecommendationService;
import com.alexandersaul.Alexander.service.RedditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reddit")
@RequiredArgsConstructor
public class RedditController {

    private final RedditService redditService;
    private final RedditMessageService redditMessageService;
    private final RecommendationService recommendationService;
    private final PostRepository postRepository;

    @PostMapping("/message/recommendation")
    public ResponseEntity<String> sendRecommendation(@RequestBody SendRecommendationRequestDto dto) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post no encontrado"));

        String recommendationText = (post.getRecommendation() != null && !post.getRecommendation().isBlank())
                ? post.getRecommendation()
                : recommendationService.askRecommendation(dto.getPostId());

        if (post.getRecommendation() == null || post.getRecommendation().isBlank()) {
            post.setRecommendation(recommendationText);
            postRepository.save(post);
        }

        String recipient = (dto.getTo() != null && !dto.getTo().isBlank()) ? dto.getTo() : post.getAuthor();
        if (recipient == null || recipient.isBlank()) {
            return ResponseEntity.badRequest().body("No se encontró el autor del post. Proporcione 'to'.");
        }

        boolean ok = redditMessageService.sendPrivateMessage(recipient,
                dto.getSubject() != null ? dto.getSubject() : "Recomendación sobre tu post",
                recommendationText);
        if (ok) {
            post.setMessageSent(true);
            postRepository.save(post);
        }
        return ResponseEntity.ok(ok ? "Mensaje enviado" : "No se pudo enviar el mensaje");
    }

}