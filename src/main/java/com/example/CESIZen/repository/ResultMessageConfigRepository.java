package com.example.CESIZen.repository;

import com.example.CESIZen.model.quiz.ResultMessageConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResultMessageConfigRepository extends JpaRepository<ResultMessageConfig, Long> {

    Optional<ResultMessageConfig> findByQuizIdAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(
            Integer quizId, Integer scoreMin, Integer scoreMax);
}
