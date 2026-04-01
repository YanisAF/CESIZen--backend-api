package com.example.CESIZen.service.result;

import com.example.CESIZen.dto.result.ResultDtoResponse;
import com.example.CESIZen.repository.ResultDiagnosisRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultQuizService {
    private final ResultDiagnosisRepository repository;

    public ResultQuizService(ResultDiagnosisRepository repository) {
        this.repository = repository;
    }

    public List<ResultDtoResponse> getAllHistoryQuiz(Long userId){
        return repository.findAllByUserId(userId)
                .stream()
                .map(entity -> new ResultDtoResponse(
                        entity.getId(),
                        entity.getTotalScore(),
                        entity.getMessage(),
                        entity.getRiskLevel(),
                        entity.getQuiz().getId(),
                        entity.getUser().getId()
                ))
                .toList();
    }
}
