package com.alexandersaul.Alexander.service.impl;

import com.alexandersaul.Alexander.service.RedditMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedditMessageServiceImpl implements RedditMessageService {

    @Value("${reddit.client.id}")
    private String clientId;

    @Value("${reddit.client.secret}")
    private String clientSecret;

    @Value("${reddit.username}")
    private String username;

    @Value("${reddit.password}")
    private String password;

    private final String userAgent = "SpringBoot:RedditClient:v1.0 (by /u/alexandersaul)";

    private String userAccessToken;
    private long tokenExpiry = 0;

    @Override
    public boolean sendPrivateMessage(String to, String subject, String text) {
        try {
            String token = getUserAccessToken();
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.set("User-Agent", "SpringBoot:RedditClient:v1.0 (by /u/alexandersaul)");
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("api_type", "json");
            form.add("to", to);
            form.add("subject", subject != null ? subject : "Recomendación de apoyo");
            form.add("text", text);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://oauth.reddit.com/api/compose",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            System.out.println("Response: " + response);
            return response.getStatusCode().is2xxSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private synchronized String getUserAccessToken() {
        long now = System.currentTimeMillis() / 1000;
        if (userAccessToken != null && now < tokenExpiry) {
            return userAccessToken;
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.set("User-Agent", userAgent);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String form = String.format("grant_type=password&username=%s&password=%s&duration=permanent&scope=privatemessages",
                encode(username), encode(password));

        HttpEntity<String> entity = new HttpEntity<>(form, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://www.reddit.com/api/v1/access_token",
                HttpMethod.POST,
                entity,
                Map.class
        );

        Map<String, Object> body = response.getBody();
        userAccessToken = (String) body.get("access_token");
        Integer expiresIn = (Integer) body.get("expires_in");
        tokenExpiry = now + (expiresIn != null ? expiresIn : 3600) - 60;

        return userAccessToken;
    }

    private String encode(String s) {
        return s.replace(" ", "+").replace("\n", "%0A");
    }
}