package com.example.CESIZen.service;

import com.example.CESIZen.dto.quiz.QuizSubmissionDto;
import com.example.CESIZen.dto.result.ResultDtoResponse;
import com.example.CESIZen.exception.ResourceNotFoundException;
import com.example.CESIZen.model.quiz.Question;
import com.example.CESIZen.model.quiz.Quiz;
import com.example.CESIZen.model.quiz.ResultDiagnosis;
import com.example.CESIZen.model.quiz.ResultMessageConfig;
import com.example.CESIZen.model.user.User;
import com.example.CESIZen.repository.QuizRepository;
import com.example.CESIZen.repository.ResultDiagnosisRepository;
import com.example.CESIZen.repository.ResultMessageConfigRepository;
import com.example.CESIZen.repository.UserRepository;
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

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private QuizScoreService quizScoreService;

    private Quiz quiz;
    private Question question1;
    private Question question2;
    private ResultMessageConfig lowConfig;
    private ResultMessageConfig highConfig;
    private User user;

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
        lowConfig.setMaxScore(4);
        lowConfig.setRiskLevel("Faible");
        lowConfig.setMessage("Votre niveau de stress est faible.");
        lowConfig.setQuiz(quiz);

        highConfig = new ResultMessageConfig();
        highConfig.setId(2L);
        highConfig.setMinScore(5);
        highConfig.setMaxScore(20);
        highConfig.setRiskLevel("Élevé");
        highConfig.setMessage("Votre niveau de stress est élevé.");
        highConfig.setQuiz(quiz);

        user = new User();
        user.setId(1L);
        user.setUserName("john.doe");
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

        ResultDtoResponse result = quizScoreService.calculateScore(1, submission);

        assertThat(result).isNotNull();
        assertThat(result.getTotalScore()).isEqualTo(3);
        assertThat(result.getRiskLevel()).isEqualTo("Faible");
        assertThat(result.getMessage()).isEqualTo("Votre niveau de stress est faible.");
        // calculateScore() ne persiste pas en base
        verifyNoInteractions(resultDiagnosisRepository);
    }

    @Test
    @DisplayName("SCR-02 | calculateScore() - Succès : score 0 si aucune réponse vraie")
    void calculateScore_shouldReturnZeroScore_whenNoTrueAnswers() throws ResourceNotFoundException {
        QuizSubmissionDto submission = new QuizSubmissionDto();
        submission.setAnswers(Map.of(1, false, 2, false));

        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));
        when(resultMessageConfigRepository
                .findByQuizIdAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(eq(1), eq(0), eq(0)))
                .thenReturn(Optional.of(lowConfig));

        ResultDtoResponse result = quizScoreService.calculateScore(1, submission);

        assertThat(result.getTotalScore()).isEqualTo(0);
        verifyNoInteractions(resultDiagnosisRepository);
    }

    @Test
    @DisplayName("SCR-03 | calculateScore() - Succès : score maximum (toutes réponses vraies)")
    void calculateScore_shouldReturnMaxScore_whenAllTrue() throws ResourceNotFoundException {
        QuizSubmissionDto submission = new QuizSubmissionDto();
        submission.setAnswers(Map.of(1, true, 2, true));

        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));
        when(resultMessageConfigRepository
                .findByQuizIdAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(eq(1), eq(7), eq(7)))
                .thenReturn(Optional.of(highConfig));

        ResultDtoResponse result = quizScoreService.calculateScore(1, submission);

        assertThat(result.getTotalScore()).isEqualTo(7);
        assertThat(result.getRiskLevel()).isEqualTo("Élevé");
        verifyNoInteractions(resultDiagnosisRepository);
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
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No message config");
    }

    @Test
    @DisplayName("SCR-06 | calculateScore() - Retourne un ResultDtoResponse sans ID ni userId")
    void calculateScore_shouldReturnDtoWithNullIdAndNullUserId() throws ResourceNotFoundException {
        QuizSubmissionDto submission = new QuizSubmissionDto();
        submission.setAnswers(Map.of(1, true, 2, false));

        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));
        when(resultMessageConfigRepository
                .findByQuizIdAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(eq(1), eq(3), eq(3)))
                .thenReturn(Optional.of(lowConfig));

        ResultDtoResponse result = quizScoreService.calculateScore(1, submission);

        assertThat(result.getId()).isNull();
        assertThat(result.getUserId()).isNull();
        assertThat(result.getQuizId()).isEqualTo(1);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  saveResult()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("SAV-01 | saveResult() - Succès : résultat sauvegardé et retourné")
    void saveResult_shouldSaveAndReturnResult() throws ResourceNotFoundException {
        QuizSubmissionDto submission = new QuizSubmissionDto();
        submission.setAnswers(Map.of(1, true, 2, false));

        ResultDiagnosis savedDiagnosis = new ResultDiagnosis();
        savedDiagnosis.setId(10);
        savedDiagnosis.setTotalScore(3);
        savedDiagnosis.setRiskLevel("Faible");
        savedDiagnosis.setMessage("Votre niveau de stress est faible.");
        savedDiagnosis.setQuiz(quiz);
        savedDiagnosis.setUser(user);

        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));
        when(userRepository.findByUserName("john.doe")).thenReturn(user);
        when(resultMessageConfigRepository
                .findByQuizIdAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(eq(1), eq(3), eq(3)))
                .thenReturn(Optional.of(lowConfig));
        when(resultDiagnosisRepository.save(any(ResultDiagnosis.class))).thenReturn(savedDiagnosis);

        ResultDtoResponse result = quizScoreService.saveResult(1, "john.doe", submission);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(10);
        assertThat(result.getTotalScore()).isEqualTo(3);
        assertThat(result.getRiskLevel()).isEqualTo("Faible");
        assertThat(result.getMessage()).isEqualTo("Votre niveau de stress est faible.");
        assertThat(result.getQuizId()).isEqualTo(1);
        assertThat(result.getUserId()).isEqualTo(1);
    }

    @Test
    @DisplayName("SAV-02 | saveResult() - Succès : résultat bien persisté en base")
    void saveResult_shouldCallRepositorySave() throws ResourceNotFoundException {
        QuizSubmissionDto submission = new QuizSubmissionDto();
        submission.setAnswers(Map.of(1, true, 2, false));

        ResultDiagnosis savedDiagnosis = new ResultDiagnosis();
        savedDiagnosis.setId(10);
        savedDiagnosis.setTotalScore(3);
        savedDiagnosis.setRiskLevel("Faible");
        savedDiagnosis.setMessage("Votre niveau de stress est faible.");
        savedDiagnosis.setQuiz(quiz);
        savedDiagnosis.setUser(user);

        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));
        when(userRepository.findByUserName("john.doe")).thenReturn(user);
        when(resultMessageConfigRepository
                .findByQuizIdAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(eq(1), eq(3), eq(3)))
                .thenReturn(Optional.of(lowConfig));
        when(resultDiagnosisRepository.save(any(ResultDiagnosis.class))).thenReturn(savedDiagnosis);

        quizScoreService.saveResult(1, "john.doe", submission);

        verify(resultDiagnosisRepository, times(1)).save(any(ResultDiagnosis.class));
    }

    @Test
    @DisplayName("SAV-03 | saveResult() - Succès : score maximum sauvegardé")
    void saveResult_shouldSaveMaxScore_whenAllTrue() throws ResourceNotFoundException {
        QuizSubmissionDto submission = new QuizSubmissionDto();
        submission.setAnswers(Map.of(1, true, 2, true));

        ResultDiagnosis savedDiagnosis = new ResultDiagnosis();
        savedDiagnosis.setId(11);
        savedDiagnosis.setTotalScore(7);
        savedDiagnosis.setRiskLevel("Élevé");
        savedDiagnosis.setMessage("Votre niveau de stress est élevé.");
        savedDiagnosis.setQuiz(quiz);
        savedDiagnosis.setUser(user);

        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));
        when(userRepository.findByUserName("john.doe")).thenReturn(user);
        when(resultMessageConfigRepository
                .findByQuizIdAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(eq(1), eq(7), eq(7)))
                .thenReturn(Optional.of(highConfig));
        when(resultDiagnosisRepository.save(any(ResultDiagnosis.class))).thenReturn(savedDiagnosis);

        ResultDtoResponse result = quizScoreService.saveResult(1, "john.doe", submission);

        assertThat(result.getTotalScore()).isEqualTo(7);
        assertThat(result.getRiskLevel()).isEqualTo("Élevé");
    }

    @Test
    @DisplayName("SAV-04 | saveResult() - Échec : quiz introuvable")
    void saveResult_shouldThrow_whenQuizNotFound() {
        when(quizRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> quizScoreService.saveResult(99, "john.doe", new QuizSubmissionDto()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Quiz not found");
    }

    @Test
    @DisplayName("SAV-05 | saveResult() - Échec : utilisateur introuvable")
    void saveResult_shouldThrow_whenUserNotFound() {
        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));
        when(userRepository.findByUserName("unknown")).thenReturn(null);

        assertThatThrownBy(() -> quizScoreService.saveResult(1, "unknown", new QuizSubmissionDto()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    @DisplayName("SAV-06 | saveResult() - Échec : aucun config de message pour ce score")
    void saveResult_shouldThrow_whenNoMessageConfig() {
        QuizSubmissionDto submission = new QuizSubmissionDto();
        submission.setAnswers(Map.of(1, true, 2, false));

        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));
        when(userRepository.findByUserName("john.doe")).thenReturn(user);
        when(resultMessageConfigRepository
                .findByQuizIdAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(anyInt(), anyInt(), anyInt()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> quizScoreService.saveResult(1, "john.doe", submission))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No message config");
    }
}