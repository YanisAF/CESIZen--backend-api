package com.example.CESIZen.repository;

import com.example.CESIZen.model.quiz.ResultMessageConfig;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ResultMessageConfigRepository extends JpaRepository<ResultMessageConfig, Long> {
    Optional<ResultMessageConfig> findByQuizIdAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(
            Integer quizId, Integer scoreMin, Integer scoreMax);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            DELETE FROM result_message_config
            WHERE quiz_id = :quizId
            """)
    void deleteAllResultMessageConfigByQuizId(Integer quizId);
}
