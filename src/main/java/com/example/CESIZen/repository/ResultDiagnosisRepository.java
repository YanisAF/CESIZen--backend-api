package com.example.CESIZen.repository;

import com.example.CESIZen.model.quiz.ResultDiagnosis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultDiagnosisRepository extends JpaRepository<ResultDiagnosis, Integer> {
    List<ResultDiagnosis> findAllByUserId(Long userId);
    void deleteAllByUserId(Long userId);
}
