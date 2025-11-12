package com.alexandersaul.Alexander.service.impl;

import com.alexandersaul.Alexander.client.OpenAIModelClient;
import com.alexandersaul.Alexander.entity.Post;
import com.alexandersaul.Alexander.repository.PostRepository;
import com.alexandersaul.Alexander.service.RecommendationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendationServiceImpl implements RecommendationService {

    private final OpenAIModelClient openAIModelClient;
    private final PostRepository postRepository;

    @Override
    public String askRecommendation(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post"));
        String prompt = "Quiero que me generes una recomendación detallada para este post de un usuario con posible depresión," +
                "Quiero que generes un mensaje con los siguientes puntos:" +
                "1. Presentate como este mensaje fue detectado de un sistema de alerta de depresión" +
                "2. Ser empático" +
                "3. Validación emocional" +
                "4. Recomendación clara y detallada" +
                "5. Cierre con tono esperanzador y humano y da enlaces de ayuda" +
                "El post es el siguiente: " + post.getContent();
        String response = openAIModelClient.ask(prompt);
        post.setRecommendation(response);
        postRepository.save(post);
        return response;
    }


}
