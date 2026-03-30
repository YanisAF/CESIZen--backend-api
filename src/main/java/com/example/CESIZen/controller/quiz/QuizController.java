package com.example.CESIZen.controller.quiz;

import com.example.CESIZen.dto.quiz.QuestionDtoRequest;
import com.example.CESIZen.dto.quiz.QuestionDtoResponse;
import com.example.CESIZen.dto.quiz.QuizDtoRequest;
import com.example.CESIZen.dto.quiz.QuizDtoResponse;
import com.example.CESIZen.exception.QuizException;
import com.example.CESIZen.exception.ResourceNotFoundException;
import com.example.CESIZen.service.quiz.QuizService;
import com.example.CESIZen.utils.Routes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping(Routes.CREATE_QUIZ)
    public ResponseEntity<QuizDtoResponse> createQuiz(@RequestBody QuizDtoRequest quizDtoRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(quizService.saveQuiz(quizDtoRequest));
    }

    @PostMapping(Routes.ADD_QUESTION_QUIZ)
    public ResponseEntity<QuizDtoResponse> addQuestionQuiz(
            @RequestBody QuestionDtoRequest questionDtoRequest,
            @RequestParam Integer quizId) throws ResourceNotFoundException, QuizException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(quizService.addQuestion(quizId, questionDtoRequest));
    }

    @GetMapping(Routes.GET_ALL_QUIZ)
    public ResponseEntity<List<QuizDtoResponse>> getQuizList(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(quizService.getAllQuiz());
    }

    @GetMapping(Routes.GET_ALL_QUESTIONS)
    public ResponseEntity<List<QuestionDtoResponse>> getAllQuestionsQuiz(@RequestParam Integer quizId) throws ResourceNotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(quizService.getAllQuestions(quizId));
    }

    @PutMapping(Routes.UPDATE_QUIZ)
    public ResponseEntity<QuizDtoResponse> updateQuiz(@RequestBody QuizDtoRequest quizDtoRequest, @RequestParam Integer id) throws ResourceNotFoundException{
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(quizService.updateQuizById(id, quizDtoRequest));
    }

    @GetMapping(Routes.GET_QUIZ)
    public ResponseEntity<QuizDtoResponse> getQuiz(@RequestParam Integer id) throws ResourceNotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(quizService.getQuizById(id));
    }

    @DeleteMapping(Routes.DELETE_QUIZ)
    public ResponseEntity<Void> deleteQuiz(@RequestParam Integer id){
        quizService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping(Routes.DELETE_QUESTION_QUIZ)
    public ResponseEntity<QuizDtoResponse> deleteQuestion(
            @RequestParam Integer quizId,
            @RequestParam Integer id) throws ResourceNotFoundException {
        quizService.deleteQuestionQuiz(quizId, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
