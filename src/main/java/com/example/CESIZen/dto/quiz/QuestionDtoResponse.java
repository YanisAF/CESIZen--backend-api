package com.example.CESIZen.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDtoResponse {
    private Integer id;
    private String statement;
    private Integer scoreValue;
}
