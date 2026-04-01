package com.example.CESIZen.controller.quiz;

import com.example.CESIZen.dto.quiz.QuizSubmissionDto;
import com.example.CESIZen.dto.result.ResultDtoResponse;
import com.example.CESIZen.exception.ResourceNotFoundException;
import com.example.CESIZen.service.quiz.QuizScoreService;
import com.example.CESIZen.service.result.ResultQuizService;
import com.example.CESIZen.utils.Routes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class QuizSubmissionController {

    private final QuizScoreService quizScoreService;
    private final ResultQuizService resultQuizService;

    public QuizSubmissionController(QuizScoreService quizScoreService,
                                    ResultQuizService resultQuizService) {
        this.quizScoreService = quizScoreService;
        this.resultQuizService = resultQuizService;
    }

    @PostMapping(Routes.SUBMIT)
    public ResponseEntity<ResultDtoResponse> submitQuiz(
            @RequestParam Integer quizId,
            @RequestBody QuizSubmissionDto submission) throws ResourceNotFoundException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(quizScoreService.calculateScore(quizId, submission));
    }

    @PostMapping(Routes.SAVE_RESULT)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResultDtoResponse> saveQuizResult(
            @RequestParam Integer quizId,
            @RequestBody QuizSubmissionDto submission,
            Authentication authentication) throws ResourceNotFoundException {

        String username = authentication.getName();
        return ResponseEntity.ok(quizScoreService.saveResult(quizId, username, submission));
    }

    @GetMapping(Routes.GET_ALL_HISTORY_QUIZ)
    public ResponseEntity<List<ResultDtoResponse>> getHistoryQuiz(
            @RequestParam Long userId) throws ResourceNotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resultQuizService.getAllHistoryQuiz(userId));
    }
}
