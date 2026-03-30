package com.example.CESIZen.service.quiz;

import com.example.CESIZen.dto.quiz.QuestionDtoRequest;
import com.example.CESIZen.dto.quiz.QuestionDtoResponse;
import com.example.CESIZen.dto.quiz.QuizDtoRequest;
import com.example.CESIZen.dto.quiz.QuizDtoResponse;
import com.example.CESIZen.exception.QuizException;
import com.example.CESIZen.exception.ResourceNotFoundException;
import com.example.CESIZen.mapper.QuestionMapper;
import com.example.CESIZen.mapper.QuizMapper;
import com.example.CESIZen.model.quiz.Question;
import com.example.CESIZen.model.quiz.Quiz;
import com.example.CESIZen.repository.QuestionRepository;
import com.example.CESIZen.repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    public QuizService(
            QuizRepository quizRepository,
            QuestionRepository questionRepository
    ) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    public QuizDtoResponse saveQuiz(QuizDtoRequest quizDtoRequest){
        Quiz quizSaved = quizRepository.save(QuizMapper.mapToEntity(quizDtoRequest));
        return QuizMapper.mapToDto(quizSaved);
    }

    public List<QuizDtoResponse> getAllQuiz(){
        return quizRepository.findAll()
                .stream()
                .map(QuizMapper::mapToDto)
                .toList();
    }

    public List<QuestionDtoResponse> getAllQuestions(Integer quizId) throws ResourceNotFoundException {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        List<QuestionDtoResponse> questionsList = quiz.getQuestionList().stream()
                .map(QuestionMapper::mapToDto)
                .toList();

        return questionsList;
    }

    public QuizDtoResponse getQuizById(Integer id) throws ResourceNotFoundException {
        return quizRepository.findById(id)
                .map(QuizMapper::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
    }

    public QuizDtoResponse updateQuizById(Integer quizId, QuizDtoRequest quizDtoRequest) throws ResourceNotFoundException {
        Quiz quizOptional = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        quizOptional.setTitle(quizDtoRequest.getTitle());
        quizOptional.setDescription(quizDtoRequest.getDescription());

        Map<Integer, Question> existingQuestions = quizOptional.getQuestionList()
                .stream()
                .collect(Collectors.toMap(Question::getId, q -> q));

        for (Question question : quizDtoRequest.getQuestionList()){

            if (question.getId() != null){
                Question existingQuestion = existingQuestions.get(question.getId());
                if (existingQuestion != null) {
                    existingQuestion.setStatement(question.getStatement());
                    existingQuestion.setScoreValue(question.getScoreValue());
                }
            }
        }
        return QuizMapper.mapToDto(quizRepository.save(quizOptional));
    }

    public void deleteById(Integer id){
        quizRepository.deleteById(id);
    }

    public QuizDtoResponse addQuestion(Integer quizId, QuestionDtoRequest questionDtoRequest) throws ResourceNotFoundException, QuizException {
        List<Question> questionList = getQuizById(quizId).getQuestionList();
        int sizeList = questionList.size();
        if (sizeList >= 15){
            throw new QuizException("The list is full or missing");
        }
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        Question newQuestion = QuestionMapper.mapToEntity(quiz, questionDtoRequest);
        questionRepository.save(newQuestion);
        return QuizMapper.mapToDto(quiz);
    }

    public void deleteQuestionQuiz(Integer quiz, Integer questionId) throws ResourceNotFoundException {
        Quiz quizUpdated = quizRepository.findById(quiz)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        List<Question> questionList = quizUpdated.getQuestionList();
        boolean removed = questionList.removeIf(q -> q.getId().equals(questionId));
        if (!removed) {
            throw new ResourceNotFoundException("Question with ID " + questionId + " not found in the quiz.");
        }
        quizRepository.save(quizUpdated);
        QuizMapper.mapToDto(quizUpdated);
    }
}
