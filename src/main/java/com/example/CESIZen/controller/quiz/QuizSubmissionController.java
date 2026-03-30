package com.example.CESIZen.controller.quiz;

import com.example.CESIZen.dto.quiz.QuizSubmissionDto;
import com.example.CESIZen.dto.result.ResultDtoResponse;
import com.example.CESIZen.exception.ResourceNotFoundException;
import com.example.CESIZen.service.quiz.QuizScoreService;
import com.example.CESIZen.utils.Routes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class QuizSubmissionController {

    private final QuizScoreService quizScoreService;

    public QuizSubmissionController(QuizScoreService quizScoreService) {
        this.quizScoreService = quizScoreService;
    }

    @PostMapping(Routes.SUBMIT)
    public ResponseEntity<ResultDtoResponse> submitQuiz(
            @RequestParam Integer quizId,
            @RequestBody QuizSubmissionDto submission) throws ResourceNotFoundException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(quizScoreService.calculateScore(quizId, submission));
    }
}
