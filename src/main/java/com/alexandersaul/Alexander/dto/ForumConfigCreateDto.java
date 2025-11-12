package com.alexandersaul.Alexander.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumConfigCreateDto {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String subreddit;
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime startDate;
    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDateTime endDate;
    @NotNull(message = "El intervalo es obligatorio")
    @Positive(message = "El intervalo debe ser mayor que 0")
    private float interval;
}