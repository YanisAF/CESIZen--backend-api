package com.example.CESIZen.mapper;

import com.example.CESIZen.dto.quiz.QuizDtoRequest;
import com.example.CESIZen.dto.quiz.QuizDtoResponse;
import com.example.CESIZen.model.quiz.Question;
import com.example.CESIZen.model.quiz.Quiz;

import java.util.List;

public class QuizMapper {

    public static Quiz mapToEntity(QuizDtoRequest quizDtoRequest){
        if (quizDtoRequest != null){
            Quiz quiz = new Quiz();
            quiz.setTitle(quizDtoRequest.getTitle());
            quiz.setDescription(quizDtoRequest.getDescription());
            System.out.println(quizDtoRequest.getQuestionList().toString());
            if (quizDtoRequest.getQuestionList() != null){
                List<Question> questionList = quizDtoRequest.getQuestionList()
                        .stream()
                        .map(question -> {
                            Question questionSaved = new Question();
                            questionSaved.setStatement(question.getStatement());
                            questionSaved.setScoreValue(question.getScoreValue());
                            questionSaved.setQuiz(quiz);
                            return questionSaved;
                        }).toList();
                quiz.setQuestionList(questionList);
            }
            return quiz;
        }
        return null;
    }

    public static QuizDtoResponse mapToDto(Quiz quiz){
        if (quiz == null) return null;

        QuizDtoResponse quizDtoResponse = new QuizDtoResponse();
        quizDtoResponse.setId(quiz.getId());
        quizDtoResponse.setTitle(quiz.getTitle());
        quizDtoResponse.setDescription(quiz.getDescription());

        if (quiz.getQuestionList() != null) {
            List<Question> questionDtoList = quiz.getQuestionList()
                    .stream()
                    .map(question -> {
                        Question questionDto = new Question();
                        questionDto.setId(question.getId());
                        questionDto.setStatement(question.getStatement());
                        questionDto.setScoreValue(question.getScoreValue());
                        return questionDto;
                    })
                    .toList();
            quizDtoResponse.setQuestionList(questionDtoList);
        }
        return quizDtoResponse;
    }
}
