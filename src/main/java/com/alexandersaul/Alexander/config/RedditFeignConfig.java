package com.alexandersaul.Alexander.config;

import feign.Request;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Configuration
public class RedditFeignConfig {

    @Value("${reddit.client.id}")
    private String clientId;

    @Value("${reddit.client.secret}")
    private String clientSecret;

    private final String userAgent = "SpringBoot:RedditClient:v1.0 (by /u/alexandersaul)";

    private String accessToken;
    private long tokenExpiry = 0;

    @Bean
    public RequestInterceptor redditRequestInterceptor() {
        return template -> {
            String token = getAccessToken();
            template.header("Authorization", "Bearer " + token);
            template.header("User-Agent", userAgent);
        };
    }

    private synchronized String getAccessToken() {
        long now = System.currentTimeMillis() / 1000;
        if (accessToken != null && now < tokenExpiry) {
            return accessToken;
        }

        String auth = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.set("User-Agent", userAgent);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>("grant_type=client_credentials", headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://www.reddit.com/api/v1/access_token",
                HttpMethod.POST,
                entity,
                Map.class
        );

        Map<String, Object> body = response.getBody();
        accessToken = (String) body.get("access_token");
        Integer expiresIn = (Integer) body.get("expires_in");
        tokenExpiry = now + expiresIn - 60; // margen de 60s

        return accessToken;
    }

    @Bean
    public Request.Options feignOptions() {
        int connectTimeoutMillis = 100000; // 10 segundos para conectar
        int readTimeoutMillis = 600000;    // 60 segundos para leer la respuesta
        return new Request.Options(connectTimeoutMillis, readTimeoutMillis);
    }
}