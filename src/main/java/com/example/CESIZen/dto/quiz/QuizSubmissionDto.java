package com.example.CESIZen.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmissionDto {
    private Integer quizId;
    private Map<Integer, Boolean> answers;
}
