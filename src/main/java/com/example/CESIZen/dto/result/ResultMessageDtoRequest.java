package com.example.CESIZen.dto.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultMessageDtoRequest {
    private Integer quizId;
    private Integer minScore;
    private Integer maxScore;
    private String riskLevel;
    private String message;
}
