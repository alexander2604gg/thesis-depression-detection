package com.alexandersaul.Alexander.dto;

import lombok.Data;

import java.util.List;

@Data
public class ModelBatchRequestDto {
    private List<ModelRequestDto> texts;
}
