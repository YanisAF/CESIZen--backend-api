package com.example.CESIZen.dto.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultDtoResponse {
    private Integer id;
    private Integer totalScore;
    private String message;
    private String riskLevel;
    private Integer quizId;
    private Long userId;
}
