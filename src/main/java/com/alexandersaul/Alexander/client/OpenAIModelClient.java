package com.alexandersaul.Alexander.client;

import com.alexandersaul.Alexander.dto.ModelBatchRequestDto;
import com.alexandersaul.Alexander.dto.PredictBatchResponseDto;
import com.alexandersaul.Alexander.dto.PredictResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OpenAIModelClient implements ModelClient {

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "gpt-4o-mini";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public PredictBatchResponseDto predict(ModelBatchRequestDto modelRequestDto) {
        String apiKey = Optional.ofNullable(System.getenv("OPENAI_API_KEY"))
                .orElseThrow(() -> new RuntimeException("OPENAI_API_KEY no está definida"));
        try {
            ArrayNode inputs = objectMapper.createArrayNode();
            for (var item : modelRequestDto.getTexts()) {
                ObjectNode node = objectMapper.createObjectNode();
                node.put("redditId", item.getRedditId());
                node.put("text", item.getText());
                inputs.add(node);
            }

            ObjectNode payload = buildRequestPayload(inputs);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OPENAI_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return parseResponse(response.body());
        } catch (Exception e) {
            // En caso de error, devolvemos una respuesta vacía para no romper el flujo
            PredictBatchResponseDto empty = new PredictBatchResponseDto();
            empty.setPredictions(new ArrayList<>());
            return empty;
        }
    }

    private ObjectNode buildRequestPayload(ArrayNode inputs) {
        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("model", MODEL);
        payload.put("temperature", 0);

        ObjectNode responseFormat = objectMapper.createObjectNode();
        responseFormat.put("type", "json_object");
        payload.set("response_format", responseFormat);

        ArrayNode messages = objectMapper.createArrayNode();

        ObjectNode system = objectMapper.createObjectNode();
        system.put("role", "system");
        system.put("content",
                "Eres un clasificador de depresión para textos de Reddit. " +
                        "Para cada entrada, produce una puntuación 'score' en [0.0,1.0] y una etiqueta 'label'. " +
                        "Usa 'LABEL_1' para señales claras de depresión y 'LABEL_0' en caso contrario. " +
                        "Responde SOLO con JSON siguiendo este esquema: {\"predictions\": [{\"redditId\": string, \"text\": string, \"prediction\": {\"label\": \"LABEL_0|LABEL_1\", \"score\": number}}] }. " +
                        "No incluyas explicaciones ni texto adicional.");
        messages.add(system);

        ObjectNode user = objectMapper.createObjectNode();
        user.put("role", "user");
        ObjectNode content = objectMapper.createObjectNode();
        content.set("inputs", inputs);
        user.put("content", content.toString());
        messages.add(user);

        payload.set("messages", messages);
        return payload;
    }

    private PredictBatchResponseDto parseResponse(String body) throws Exception {
        JsonNode root = objectMapper.readTree(body);
        String content = root.path("choices").path(0).path("message").path("content").asText();

        JsonNode json = objectMapper.readTree(content);
        ArrayNode predictionsNode = (ArrayNode) json.path("predictions");

        List<PredictResponseDto> predictions = new ArrayList<>();
        for (JsonNode node : predictionsNode) {
            PredictResponseDto pr = new PredictResponseDto();
            pr.setRedditId(node.path("redditId").asText());
            pr.setText(node.path("text").asText());

            PredictResponseDto.Prediction pred = new PredictResponseDto.Prediction();
            pred.setLabel(node.path("prediction").path("label").asText());
            pred.setScore(node.path("prediction").path("score").asDouble());
            pr.setPrediction(pred);

            predictions.add(pr);
        }

        PredictBatchResponseDto result = new PredictBatchResponseDto();
        result.setPredictions(predictions);
        return result;
    }

    public String ask(String prompt) {
        String apiKey = Optional.ofNullable(System.getenv("OPENAI_API_KEY"))
                .orElseThrow(() -> new RuntimeException("OPENAI_API_KEY no está definida"));

        try {
            ObjectNode payload = buildChatPayload(prompt);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OPENAI_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode root = objectMapper.readTree(response.body());
            return root.path("choices").path(0).path("message").path("content").asText();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private ObjectNode buildChatPayload(String prompt) {
        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("model", MODEL);
        payload.put("temperature", 0.2);

        ArrayNode messages = objectMapper.createArrayNode();

        ObjectNode system = objectMapper.createObjectNode();
        system.put("role", "system");
        system.put("content", "Eres un asistente útil. Responde de forma breve y clara.");
        messages.add(system);

        ObjectNode user = objectMapper.createObjectNode();
        user.put("role", "user");
        user.put("content", prompt);
        messages.add(user);

        payload.set("messages", messages);
        return payload;
    }
}