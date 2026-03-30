package com.example.CESIZen.dto.quiz;

import com.example.CESIZen.model.quiz.Question;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizDtoRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 32, message = "title is empty")
    @JsonProperty("title")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 3, max = 32, message = "description is empty")
    @JsonProperty("description")
    private String description;

    private List<Question> questionList;
}
