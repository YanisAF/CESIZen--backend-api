package com.example.CESIZen.repository;

import com.example.CESIZen.model.quiz.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
