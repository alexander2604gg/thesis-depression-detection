package com.alexandersaul.Alexander.service.impl;

import com.alexandersaul.Alexander.client.ModelClient;
import com.alexandersaul.Alexander.dto.ModelBatchRequestDto;
import com.alexandersaul.Alexander.dto.ModelRequestDto;
import com.alexandersaul.Alexander.dto.PredictBatchResponseDto;
import com.alexandersaul.Alexander.dto.PredictResponseDto;
import com.alexandersaul.Alexander.entity.Analysis;
import com.alexandersaul.Alexander.entity.Post;
import com.alexandersaul.Alexander.repository.AnalysisRepository;
import com.alexandersaul.Alexander.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AnalysisService implements com.alexandersaul.Alexander.service.AnalysisService {

    private final ModelClient modelClient;
    private final PostRepository postRepository;
    private final AnalysisRepository analysisRepository;

    @Async
    @Override
    public void analyzePosts(List<Post> posts) {
        log.info("Iniciando análisis de {} posts en segundo plano...", posts.size());

        if (posts == null || posts.isEmpty()) {
            return;
        }

        Map<String, Post> postsByRedditId = posts.stream()
                .collect(Collectors.toMap(Post::getRedditId, Function.identity()));

        List<ModelRequestDto> requests = posts.stream()
                .map(post -> {
                    ModelRequestDto dto = new ModelRequestDto();
                    dto.setRedditId(post.getRedditId());
                    dto.setText(post.getContent());
                    return dto;
                })
                .toList();

        ModelBatchRequestDto batchRequest = new ModelBatchRequestDto();
        batchRequest.setTexts(requests);

        try {
            PredictBatchResponseDto response = modelClient.predict(batchRequest);

            List<Post> updatedPosts = new ArrayList<>();
            for (PredictResponseDto pred : response.getPredictions()) {
                Analysis analysis = new Analysis();
                Analysis savedAnalysis = null;
                if(pred.getPrediction()!=null){
                    analysis.setDepressionScore(pred.getPrediction().getScore());
                    analysis.setModelVersion("Modelito de Alexander Saul");
                    analysis.setAnalyzedAt(LocalDateTime.now());
                    analysis.setLabel(pred.getPrediction().getLabel());
                    savedAnalysis = analysisRepository.save(analysis);
                }

                Post post = postsByRedditId.get(pred.getRedditId());
                if (post != null) {
                    post.setAnalysis(savedAnalysis);
                    updatedPosts.add(post);
                }
            }

            if (!updatedPosts.isEmpty()) {
                postRepository.saveAll(updatedPosts);
            }

            log.info("Análisis completado correctamente para {} posts", updatedPosts.size());
        } catch (Exception e) {
            log.error("Error al consumir el modelo de predicciones", e);
        }
    }


}
