package com.example.CESIZen.service;

import com.example.CESIZen.dto.quiz.QuestionDtoRequest;
import com.example.CESIZen.dto.quiz.QuizDtoRequest;
import com.example.CESIZen.dto.quiz.QuizDtoResponse;
import com.example.CESIZen.exception.QuizException;
import com.example.CESIZen.exception.ResourceNotFoundException;
import com.example.CESIZen.model.quiz.Question;
import com.example.CESIZen.model.quiz.Quiz;
import com.example.CESIZen.repository.QuestionRepository;
import com.example.CESIZen.repository.QuizRepository;
import com.example.CESIZen.service.quiz.QuizService;
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
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - QuizService")
class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuizService quizService;

    private Quiz quiz;
    private QuizDtoRequest quizRequest;

    @BeforeEach
    void setUp() {
        quiz = new Quiz();
        quiz.setId(1);
        quiz.setTitle("Test de stress");
        quiz.setDescription("Évaluation du niveau de stress");
        quiz.setQuestionList(new ArrayList<>());

        quizRequest = new QuizDtoRequest();
        quizRequest.setTitle("Test de stress");
        quizRequest.setDescription("Évaluation du niveau de stress");
        quizRequest.setQuestionList(new ArrayList<>());
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  saveQuiz()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("QZ-01 | saveQuiz() - Succès : quiz créé")
    void saveQuiz_shouldReturnDto_whenValid() {
        when(quizRepository.save(any(Quiz.class))).thenReturn(quiz);

        QuizDtoResponse result = quizService.saveQuiz(quizRequest);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test de stress");
        verify(quizRepository).save(any(Quiz.class));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  getAllQuiz()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("QZ-02 | getAllQuiz() - Retourne tous les quiz")
    void getAllQuiz_shouldReturnAll() {
        Quiz quiz2 = new Quiz();
        quiz2.setId(2);
        quiz2.setTitle("Test d'anxiété");
        quiz2.setQuestionList(new ArrayList<>());

        when(quizRepository.findAll()).thenReturn(List.of(quiz, quiz2));

        List<QuizDtoResponse> result = quizService.getAllQuiz();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(QuizDtoResponse::getTitle)
                .containsExactlyInAnyOrder("Test de stress", "Test d'anxiété");
    }

    @Test
    @DisplayName("QZ-03 | getAllQuiz() - Liste vide si aucun quiz")
    void getAllQuiz_shouldReturnEmpty_whenNone() {
        when(quizRepository.findAll()).thenReturn(List.of());

        List<QuizDtoResponse> result = quizService.getAllQuiz();

        assertThat(result).isEmpty();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  getQuizById()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("QZ-04 | getQuizById() - Succès : quiz trouvé")
    void getQuizById_shouldReturnDto_whenFound() throws ResourceNotFoundException {
        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));

        QuizDtoResponse result = quizService.getQuizById(1);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test de stress");
    }

    @Test
    @DisplayName("QZ-05 | getQuizById() - Échec : quiz introuvable")
    void getQuizById_shouldThrow_whenNotFound() {
        when(quizRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> quizService.getQuizById(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Resource not found");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  getAllQuestions()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("QZ-06 | getAllQuestions() - Retourne questions d'un quiz")
    void getAllQuestions_shouldReturnQuestions() throws ResourceNotFoundException {
        Question q1 = new Question();
        q1.setId(1);
        q1.setStatement("Vous sentez-vous souvent stressé ?");
        q1.setScoreValue(2);
        quiz.getQuestionList().add(q1);

        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));

        var result = quizService.getAllQuestions(1);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatement()).isEqualTo("Vous sentez-vous souvent stressé ?");
    }

    @Test
    @DisplayName("QZ-07 | getAllQuestions() - Échec : quiz introuvable")
    void getAllQuestions_shouldThrow_whenQuizNotFound() {
        when(quizRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> quizService.getAllQuestions(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  addQuestion()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("QZ-08 | addQuestion() - Succès : question ajoutée")
    void addQuestion_shouldAdd_whenQuizHasSpace() throws ResourceNotFoundException, QuizException {
        QuestionDtoRequest questionRequest = new QuestionDtoRequest();
        questionRequest.setStatement("Dormez-vous bien ?");
        questionRequest.setScoreValue(1);

        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));
        when(questionRepository.save(any(Question.class))).thenAnswer(inv -> inv.getArgument(0));

        QuizDtoResponse result = quizService.addQuestion(1, questionRequest);

        assertThat(result).isNotNull();
        verify(questionRepository).save(any(Question.class));
    }

    @Test
    @DisplayName("QZ-09 | addQuestion() - Échec : quiz plein (>=15 questions)")
    void addQuestion_shouldThrow_whenQuizIsFull() {
        List<Question> fullList = IntStream.range(0, 15)
                .mapToObj(i -> {
                    Question q = new Question();
                    q.setId(i);
                    q.setStatement("Question " + i);
                    q.setScoreValue(1);
                    return q;
                })
                .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
        quiz.setQuestionList(fullList);

        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));

        assertThatThrownBy(() -> quizService.addQuestion(1, new QuestionDtoRequest()))
                .isInstanceOf(QuizException.class)
                .hasMessageContaining("full");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  updateQuizById()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("QZ-10 | updateQuizById() - Succès : titre et description mis à jour")
    void updateQuizById_shouldUpdateFields() throws ResourceNotFoundException {
        quizRequest.setTitle("Nouveau titre");
        quizRequest.setDescription("Nouvelle description");

        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));
        when(quizRepository.save(any(Quiz.class))).thenAnswer(inv -> {
            Quiz q = inv.getArgument(0);
            q.setTitle("Nouveau titre");
            q.setDescription("Nouvelle description");
            return q;
        });

        QuizDtoResponse result = quizService.updateQuizById(1, quizRequest);

        assertThat(result.getTitle()).isEqualTo("Nouveau titre");
    }

    @Test
    @DisplayName("QZ-11 | updateQuizById() - Échec : quiz introuvable")
    void updateQuizById_shouldThrow_whenNotFound() {
        when(quizRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> quizService.updateQuizById(99, quizRequest))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  deleteById()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("QZ-12 | deleteById() - Succès : suppression effective")
    void deleteById_shouldCallRepository() {
        doNothing().when(quizRepository).deleteById(1);

        assertThatNoException().isThrownBy(() -> quizService.deleteById(1));

        verify(quizRepository).deleteById(1);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  deleteQuestionQuiz()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("QZ-13 | deleteQuestionQuiz() - Succès : question supprimée")
    void deleteQuestionQuiz_shouldRemoveQuestion() throws ResourceNotFoundException {
        Question q = new Question();
        q.setId(10);
        q.setStatement("Question à supprimer");
        quiz.getQuestionList().add(q);

        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));
        when(quizRepository.save(any(Quiz.class))).thenReturn(quiz);

        assertThatNoException().isThrownBy(() -> quizService.deleteQuestionQuiz(1, 10));
        assertThat(quiz.getQuestionList()).isEmpty();
    }

    @Test
    @DisplayName("QZ-14 | deleteQuestionQuiz() - Échec : question inexistante dans le quiz")
    void deleteQuestionQuiz_shouldThrow_whenQuestionNotInQuiz() {
        when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));

        assertThatThrownBy(() -> quizService.deleteQuestionQuiz(1, 999))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }
}
