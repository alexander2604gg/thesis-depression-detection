package com.alexandersaul.Alexander.client;

import com.alexandersaul.Alexander.dto.ModelBatchRequestDto;
import com.alexandersaul.Alexander.dto.PredictBatchResponseDto;

public interface ModelClient {
    PredictBatchResponseDto predict(ModelBatchRequestDto modelRequestDto);
}
