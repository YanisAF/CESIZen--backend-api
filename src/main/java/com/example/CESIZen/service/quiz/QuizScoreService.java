package com.example.CESIZen.service.quiz;

import com.example.CESIZen.dto.quiz.QuizSubmissionDto;
import com.example.CESIZen.dto.result.ResultDtoResponse;
import com.example.CESIZen.exception.ResourceNotFoundException;
import com.example.CESIZen.model.quiz.Question;
import com.example.CESIZen.model.quiz.Quiz;
import com.example.CESIZen.model.quiz.ResultDiagnosis;
import com.example.CESIZen.model.quiz.ResultMessageConfig;
import com.example.CESIZen.repository.QuizRepository;
import com.example.CESIZen.repository.ResultDiagnosisRepository;
import com.example.CESIZen.repository.ResultMessageConfigRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class QuizScoreService {

    private final QuizRepository quizRepository;
    private final ResultDiagnosisRepository repository;
    private final ResultMessageConfigRepository resultMessageConfigRepository;

    public QuizScoreService(QuizRepository quizRepository,
                            ResultDiagnosisRepository repository,
                            ResultMessageConfigRepository resultMessageConfigRepository) {
        this.quizRepository = quizRepository;
        this.repository = repository;
        this.resultMessageConfigRepository = resultMessageConfigRepository;
    }

    public ResultDtoResponse calculateScore(Integer quizId, QuizSubmissionDto submission) throws ResourceNotFoundException{
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        int score = quiz.getQuestionList()
                .stream()
                .filter(q -> Boolean.TRUE.equals(submission.getAnswers().get(q.getId())))
                .mapToInt(Question::getScoreValue)
                .sum();

        ResultMessageConfig config = resultMessageConfigRepository
                .findByQuizIdAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(quizId, score, score)
                .orElseThrow(() -> new RuntimeException("No message config for this score"));

        String riskLevel = config.getRiskLevel();
        String message = config.getMessage();

        ResultDiagnosis result = new ResultDiagnosis();
        result.setQuiz(quiz);
        result.setTotalScore(score);
        result.setRiskLevel(riskLevel);
        result.setMessage(message);
        result.setCreatedAt(LocalDateTime.now());

        ResultDiagnosis savedResult = repository.save(result);

        return new ResultDtoResponse(
                savedResult.getId(),
                savedResult.getTotalScore(),
                savedResult.getMessage(),
                savedResult.getRiskLevel(),
                savedResult.getQuiz() != null ? savedResult.getQuiz().getId() : null,
                savedResult.getUser() != null ? savedResult.getUser().getId() : null
        );
    }
}
