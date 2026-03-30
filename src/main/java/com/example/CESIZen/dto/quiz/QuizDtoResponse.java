package com.example.CESIZen.dto.quiz;

import com.example.CESIZen.model.quiz.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizDtoResponse {

    private Integer id;
    private String title;
    private String description;
    private List<Question> questionList;
}
