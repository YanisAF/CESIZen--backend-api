package com.example.CESIZen.service;

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
import com.example.CESIZen.service.quiz.QuizScoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - QuizScoreService")
class QuizScoreServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private ResultDiagnosisRepository resultDiagnosisRepository;

    @Mock
    private ResultMessageConfigRepository resultMessageConfigRepository;

    @InjectMocks
    private QuizScoreService quizScoreService;

    private Quiz quiz;
    private Question question1;
    private Question question2;
    private ResultMessageConfig lowConfig;
    private ResultDiagnosis savedDiagnosis;

    @BeforeEach
    void setUp() {
        question1 = new Question();
        question1.setId(1);
        question1.setStatement("Vous sentez-vous fatigué ?");
        question1.setScoreValue(3);

        question2 = new Question();
        question2.setId(2);
        question2.setStatement("Dormez-vous moins de 6h par nuit ?");
        question2.setScoreValue(4);

        quiz = new Quiz();
        quiz.setId(1);
        quiz.setTitle("Test de stress");
        quiz.setQuestionList(new ArrayList<>(List.of(question1, question2)));

        lowConfig = new ResultMessageConfig();
        lowConfig.setId(1L);
        lowConfig.setMinScore(0);
        lowConfig.setMaxScore(10);
        lowConfig.setRiskLevel("Faible");
        lowConfig.setMessage("Votre niveau de stress est faible.");
        lowConfig.setQuiz(quiz);

        savedDiagnosis = new ResultDiagnosis();
        savedDiagnosis.setId(1);
        savedDiagnosis.setTotalScore(3);
        savedDiagnosis.setRiskLevel("Faible");
        savedDiagnosis.setMessage("Votre niveau de stress est faible.");
        savedDiagnosis.setQuiz(quiz);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  calculateScore()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("SCR-01 | calculateScore() - Succès : score calculé avec une réponse vraie")
    void calculateScore_shouldComputeCorrectScore_oneTrueAnswer() throws ResourceNotFoundException {
        QuizSubmissionDto submission = new QuizSubmissionDto();
        submission.setAnswers(Map.of(1, true, 2, false));

        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));
        when(resultMessageConfigRepository
                .findByQuizIdAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(eq(1), eq(3), eq(3)))
                .thenReturn(Optional.of(lowConfig));
        when(resultDiagnosisRepository.save(any(ResultDiagnosis.class))).thenReturn(savedDiagnosis);

        ResultDtoResponse result = quizScoreService.calculateScore(1, submission);

        assertThat(result).isNotNull();
        assertThat(result.getTotalScore()).isEqualTo(3);
        assertThat(result.getRiskLevel()).isEqualTo("Faible");
        assertThat(result.getMessage()).isEqualTo("Votre niveau de stress est faible.");
    }

    @Test
    @DisplayName("SCR-02 | calculateScore() - Succès : score 0 si aucune réponse vraie")
    void calculateScore_shouldReturnZeroScore_whenNoTrueAnswers() throws ResourceNotFoundException {
        QuizSubmissionDto submission = new QuizSubmissionDto();
        submission.setAnswers(Map.of(1, false, 2, false));

        ResultMessageConfig zeroConfig = new ResultMessageConfig();
        zeroConfig.setId(2L);
        zeroConfig.setMinScore(0);
        zeroConfig.setMaxScore(0);
        zeroConfig.setRiskLevel("Nul");
        zeroConfig.setMessage("Aucun stress détecté.");
        zeroConfig.setQuiz(quiz);

        ResultDiagnosis zeroDiagnosis = new ResultDiagnosis();
        zeroDiagnosis.setId(2);
        zeroDiagnosis.setTotalScore(0);
        zeroDiagnosis.setRiskLevel("Nul");
        zeroDiagnosis.setMessage("Aucun stress détecté.");
        zeroDiagnosis.setQuiz(quiz);

        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));
        when(resultMessageConfigRepository
                .findByQuizIdAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(eq(1), eq(0), eq(0)))
                .thenReturn(Optional.of(zeroConfig));
        when(resultDiagnosisRepository.save(any(ResultDiagnosis.class))).thenReturn(zeroDiagnosis);

        ResultDtoResponse result = quizScoreService.calculateScore(1, submission);

        assertThat(result.getTotalScore()).isEqualTo(0);
    }

    @Test
    @DisplayName("SCR-03 | calculateScore() - Succès : score maximum (toutes réponses vraies)")
    void calculateScore_shouldReturnMaxScore_whenAllTrue() throws ResourceNotFoundException {
        QuizSubmissionDto submission = new QuizSubmissionDto();
        submission.setAnswers(Map.of(1, true, 2, true));

        ResultMessageConfig highConfig = new ResultMessageConfig();
        highConfig.setId(3L);
        highConfig.setMinScore(5);
        highConfig.setMaxScore(20);
        highConfig.setRiskLevel("Élevé");
        highConfig.setMessage("Votre niveau de stress est élevé.");
        highConfig.setQuiz(quiz);

        ResultDiagnosis highDiagnosis = new ResultDiagnosis();
        highDiagnosis.setId(3);
        highDiagnosis.setTotalScore(7);
        highDiagnosis.setRiskLevel("Élevé");
        highDiagnosis.setMessage("Votre niveau de stress est élevé.");
        highDiagnosis.setQuiz(quiz);

        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));
        when(resultMessageConfigRepository
                .findByQuizIdAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(eq(1), eq(7), eq(7)))
                .thenReturn(Optional.of(highConfig));
        when(resultDiagnosisRepository.save(any(ResultDiagnosis.class))).thenReturn(highDiagnosis);

        ResultDtoResponse result = quizScoreService.calculateScore(1, submission);

        assertThat(result.getTotalScore()).isEqualTo(7);
        assertThat(result.getRiskLevel()).isEqualTo("Élevé");
    }

    @Test
    @DisplayName("SCR-04 | calculateScore() - Échec : quiz introuvable")
    void calculateScore_shouldThrow_whenQuizNotFound() {
        when(quizRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> quizScoreService.calculateScore(99, new QuizSubmissionDto()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Quiz not found");
    }

    @Test
    @DisplayName("SCR-05 | calculateScore() - Échec : aucun config de message pour ce score")
    void calculateScore_shouldThrow_whenNoMessageConfig() {
        QuizSubmissionDto submission = new QuizSubmissionDto();
        submission.setAnswers(Map.of(1, true, 2, false));

        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));
        when(resultMessageConfigRepository
                .findByQuizIdAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(anyInt(), anyInt(), anyInt()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> quizScoreService.calculateScore(1, submission))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No message config");
    }

    @Test
    @DisplayName("SCR-06 | calculateScore() - Réponse persistée en base")
    void calculateScore_shouldSaveResult() throws ResourceNotFoundException {
        QuizSubmissionDto submission = new QuizSubmissionDto();
        submission.setAnswers(Map.of(1, true, 2, false));

        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));
        when(resultMessageConfigRepository
                .findByQuizIdAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(eq(1), eq(3), eq(3)))
                .thenReturn(Optional.of(lowConfig));
        when(resultDiagnosisRepository.save(any(ResultDiagnosis.class))).thenReturn(savedDiagnosis);

        quizScoreService.calculateScore(1, submission);

        verify(resultDiagnosisRepository).save(any(ResultDiagnosis.class));
    }
}
