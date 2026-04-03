package com.example.CESIZen.mapper;

import com.example.CESIZen.dto.quiz.QuestionDtoRequest;
import com.example.CESIZen.dto.quiz.QuestionDtoResponse;
import com.example.CESIZen.model.quiz.Question;
import com.example.CESIZen.model.quiz.Quiz;

public class QuestionMapper {

    public static Question mapToEntity(Quiz quiz, QuestionDtoRequest questionDtoRequest){
        if (questionDtoRequest != null){
            Question question = new Question();
            question.setStatement(questionDtoRequest.getStatement());
            question.setScoreValue(questionDtoRequest.getScoreValue());
            question.setQuiz(quiz);
            return question;
        }
        return null;
    }

    public static QuestionDtoResponse mapToDto(Question question){
        if (question != null){
            QuestionDtoResponse questionDtoResponse = new QuestionDtoResponse();
            questionDtoResponse.setId(question.getId());
            questionDtoResponse.setStatement(question.getStatement());
            questionDtoResponse.setScoreValue(question.getScoreValue());
            return questionDtoResponse;
        }
        return null;
    }
}
