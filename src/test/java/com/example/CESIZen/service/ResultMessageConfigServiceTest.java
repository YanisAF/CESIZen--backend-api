package com.example.CESIZen.service;

import com.example.CESIZen.dto.result.ResultMessageDtoRequest;
import com.example.CESIZen.dto.result.ResultMessageDtoResponse;
import com.example.CESIZen.model.quiz.Quiz;
import com.example.CESIZen.model.quiz.ResultMessageConfig;
import com.example.CESIZen.repository.QuizRepository;
import com.example.CESIZen.repository.ResultMessageConfigRepository;
import com.example.CESIZen.service.quiz.ResultMessageConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - ResultMessageConfigService")
class ResultMessageConfigServiceTest {

    @Mock private ResultMessageConfigRepository repository;
    @Mock private QuizRepository quizRepository;

    @InjectMocks
    private ResultMessageConfigService service;

    private Quiz quiz;
    private ResultMessageConfig config;
    private ResultMessageDtoRequest dto;

    @BeforeEach
    void setUp() {
        quiz = new Quiz();
        quiz.setId(1);
        quiz.setTitle("Test de stress");
        quiz.setQuestionList(new ArrayList<>());

        config = new ResultMessageConfig();
        config.setId(1L);
        config.setQuiz(quiz);
        config.setMinScore(0);
        config.setMaxScore(5);
        config.setRiskLevel("Faible");
        config.setMessage("Stress faible, continuez ainsi !");

        dto = new ResultMessageDtoRequest();
        dto.setQuizId(1);
        dto.setMinScore(0);
        dto.setMaxScore(5);
        dto.setRiskLevel("Faible");
        dto.setMessage("Stress faible, continuez ainsi !");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  createConfig()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("RMC-01 | createConfig() - Succès : configuration créée")
    void createConfig_shouldReturnDto_whenValid() {
        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));
        when(repository.save(any(ResultMessageConfig.class))).thenReturn(config);

        ResultMessageDtoResponse result = service.createConfig(dto);

        assertThat(result).isNotNull();
        assertThat(result.getRiskLevel()).isEqualTo("Faible");
        assertThat(result.getMinScore()).isEqualTo(0);
        assertThat(result.getMaxScore()).isEqualTo(5);
        assertThat(result.getMessage()).isEqualTo("Stress faible, continuez ainsi !");
    }

    @Test
    @DisplayName("RMC-02 | createConfig() - Échec : quiz introuvable")
    void createConfig_shouldThrow_whenQuizNotFound() {
        when(quizRepository.findById(99)).thenReturn(Optional.empty());
        dto.setQuizId(99);

        assertThatThrownBy(() -> service.createConfig(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Quiz not found");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  updateConfig()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("RMC-03 | updateConfig() - Succès : message mis à jour")
    void updateConfig_shouldUpdateFields() {
        dto.setMessage("Nouveau message");
        dto.setRiskLevel("Modéré");
        dto.setQuizId(null); // pas de changement de quiz

        ResultMessageConfig updated = new ResultMessageConfig();
        updated.setId(1L);
        updated.setQuiz(quiz);
        updated.setMinScore(0);
        updated.setMaxScore(5);
        updated.setRiskLevel("Modéré");
        updated.setMessage("Nouveau message");

        when(repository.findById(1L)).thenReturn(Optional.of(config));
        when(repository.save(any(ResultMessageConfig.class))).thenReturn(updated);

        ResultMessageDtoResponse result = service.updateConfig(1L, dto);

        assertThat(result.getMessage()).isEqualTo("Nouveau message");
        assertThat(result.getRiskLevel()).isEqualTo("Modéré");
    }

    @Test
    @DisplayName("RMC-04 | updateConfig() - Échec : config introuvable")
    void updateConfig_shouldThrow_whenConfigNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateConfig(99L, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Config not found");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  getAllConfigs()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("RMC-05 | getAllConfigs() - Retourne toutes les configurations")
    void getAllConfigs_shouldReturnAll() {
        ResultMessageConfig config2 = new ResultMessageConfig();
        config2.setId(2L);
        config2.setQuiz(quiz);
        config2.setMinScore(6);
        config2.setMaxScore(15);
        config2.setRiskLevel("Élevé");
        config2.setMessage("Stress élevé, consultez un professionnel.");

        when(repository.findAll()).thenReturn(List.of(config, config2));

        List<ResultMessageDtoResponse> result = service.getAllConfigs();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(ResultMessageDtoResponse::getRiskLevel)
                .containsExactlyInAnyOrder("Faible", "Élevé");
    }

    @Test
    @DisplayName("RMC-06 | getAllConfigs() - Retourne liste vide")
    void getAllConfigs_shouldReturnEmpty() {
        when(repository.findAll()).thenReturn(List.of());

        List<ResultMessageDtoResponse> result = service.getAllConfigs();

        assertThat(result).isEmpty();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  deleteConfig()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("RMC-07 | deleteConfig() - Succès : suppression effective")
    void deleteConfig_shouldCallRepository() {
        doNothing().when(repository).deleteById(1L);

        assertThatNoException().isThrownBy(() -> service.deleteConfig(1L));

        verify(repository).deleteById(1L);
    }
}
