package com.example.CESIZen.model.quiz;

import com.example.CESIZen.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "result_diagnosis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultDiagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer totalScore;
    private String message;
    private String riskLevel;

    @ManyToOne(optional = true)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    private LocalDateTime createdAt;
}

