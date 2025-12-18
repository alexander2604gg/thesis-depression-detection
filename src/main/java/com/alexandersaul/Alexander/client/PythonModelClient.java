package com.alexandersaul.Alexander.client;

import com.alexandersaul.Alexander.dto.ModelBatchRequestDto;
import com.alexandersaul.Alexander.dto.PredictBatchResponseDto;
import com.alexandersaul.Alexander.dto.PredictResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PythonModelClient implements ModelClient {

    @Value("${python.model.url}")
    private String pythonModelUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public PredictBatchResponseDto predict(ModelBatchRequestDto modelRequestDto) {

        try {
            // ===== construir payload EXACTO que espera FastAPI =====
            ObjectNode payload = objectMapper.createObjectNode();
            ArrayNode textsNode = objectMapper.createArrayNode();

            for (var item : modelRequestDto.getTexts()) {
                ObjectNode node = objectMapper.createObjectNode();
                node.put("redditId", item.getRedditId());
                node.put("text", item.getText());
                textsNode.add(node);
            }

            payload.set("texts", textsNode);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(pythonModelUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return parseResponse(response.body());

        } catch (Exception e) {
            // fallback seguro
            PredictBatchResponseDto empty = new PredictBatchResponseDto();
            empty.setPredictions(new ArrayList<>());
            return empty;
        }
    }

    private PredictBatchResponseDto parseResponse(String body) throws Exception {
        JsonNode root = objectMapper.readTree(body);
        ArrayNode predictionsNode = (ArrayNode) root.path("predictions");

        List<PredictResponseDto> predictions = new ArrayList<>();

        for (JsonNode node : predictionsNode) {
            PredictResponseDto pr = new PredictResponseDto();
            pr.setRedditId(node.path("redditId").asText());
            pr.setText(node.path("text").asText());

            JsonNode predNode = node.path("prediction");

            if (!predNode.isMissingNode() && !predNode.isNull()) {
                PredictResponseDto.Prediction pred =
                        new PredictResponseDto.Prediction();

                pred.setLabel(predNode.path("label").asText());
                pred.setScore(predNode.path("score").asDouble());

                pr.setPrediction(pred);
            }

            predictions.add(pr);
        }

        PredictBatchResponseDto result = new PredictBatchResponseDto();
        result.setPredictions(predictions);
        return result;
    }
}
