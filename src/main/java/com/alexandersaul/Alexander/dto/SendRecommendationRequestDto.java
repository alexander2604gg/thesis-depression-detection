package com.alexandersaul.Alexander.dto;

import lombok.Data;

@Data
public class SendRecommendationRequestDto {
    private Long postId;
    private String to; // nombre de usuario en Reddit (sin /u/)
    private String subject; // opcional, por defecto se usará uno genérico
}