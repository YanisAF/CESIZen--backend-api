package com.example.CESIZen.model.quiz;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "result_message_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultMessageConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    private Integer minScore;
    private Integer maxScore;

    private String riskLevel;
    private String message;
}

