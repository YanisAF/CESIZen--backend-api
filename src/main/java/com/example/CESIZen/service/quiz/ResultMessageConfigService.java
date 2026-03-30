package com.example.CESIZen.service.quiz;

import com.example.CESIZen.dto.result.ResultMessageDtoRequest;
import com.example.CESIZen.dto.result.ResultMessageDtoResponse;
import com.example.CESIZen.model.quiz.Quiz;
import com.example.CESIZen.model.quiz.ResultMessageConfig;
import com.example.CESIZen.repository.QuizRepository;
import com.example.CESIZen.repository.ResultMessageConfigRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultMessageConfigService {

    private final ResultMessageConfigRepository repository;
    private final QuizRepository quizRepository;

    public ResultMessageConfigService(ResultMessageConfigRepository repository,
                                      QuizRepository quizRepository) {
        this.repository = repository;
        this.quizRepository = quizRepository;
    }

    public ResultMessageDtoResponse createConfig(ResultMessageDtoRequest dto) {
        Quiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        ResultMessageConfig config = new ResultMessageConfig();
        config.setQuiz(quiz);
        config.setMinScore(dto.getMinScore());
        config.setMaxScore(dto.getMaxScore());
        config.setRiskLevel(dto.getRiskLevel());
        config.setMessage(dto.getMessage());

        ResultMessageConfig saved = repository.save(config);

        return mapToDto(saved);
    }

    public ResultMessageDtoResponse updateConfig(Long id, ResultMessageDtoRequest dto) {
        ResultMessageConfig config = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Config not found"));

        if (dto.getQuizId() != null) {
            Quiz quiz = quizRepository.findById(dto.getQuizId())
                    .orElseThrow(() -> new RuntimeException("Quiz not found"));
            config.setQuiz(quiz);
        }
        if (dto.getMinScore() != null) config.setMinScore(dto.getMinScore());
        if (dto.getMaxScore() != null) config.setMaxScore(dto.getMaxScore());
        if (dto.getRiskLevel() != null) config.setRiskLevel(dto.getRiskLevel());
        if (dto.getMessage() != null) config.setMessage(dto.getMessage());

        ResultMessageConfig saved = repository.save(config);
        return mapToDto(saved);
    }

    public List<ResultMessageDtoResponse> getAllConfigs() {
        return repository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void deleteConfig(Long id) {
        repository.deleteById(id);
    }

    private ResultMessageDtoResponse mapToDto(ResultMessageConfig config) {
        return new ResultMessageDtoResponse(
                config.getId(),
                config.getQuiz().getId(),
                config.getMinScore(),
                config.getMaxScore(),
                config.getRiskLevel(),
                config.getMessage()
        );
    }
}
