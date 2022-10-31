package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("질문을 저장한다")
    void save_question_test() {
        Question question = QuestionTest.Q1;

        Question saveQuestion = questionRepository.save(question);

        assertAll(
            () -> assertThat(saveQuestion.getId()).isNotNull(),
            () -> assertThat(saveQuestion.getTitle()).isEqualTo(question.getTitle()),
            () -> assertThat(saveQuestion.getContents()).isEqualTo(question.getContents()),
            () -> assertThat(saveQuestion.getWriterId()).isEqualTo(question.getWriterId()),
            () -> assertThat(saveQuestion.getCreatedAt()).isNotNull()
        );
    }

    @Test
    @DisplayName("전체 질문을 조회한다.")
    void query_all_questions_test() {
        List<Question> questions = Arrays.asList(QuestionTest.Q1, QuestionTest.Q2);
        questionRepository.saveAll(questions);

        List<Question> findAllQuestionByDeletedFalse = questionRepository.findByDeletedFalse();

        assertThat(findAllQuestionByDeletedFalse.size()).isEqualTo(questions.size());
    }

    @Test
    @DisplayName("삭제된 질문은 조회되지 않는다.")
    void delete_question_test() {
        List<Question> questions = questionRepository.saveAll(Arrays.asList(QuestionTest.Q1, QuestionTest.Q2));

        questionRepository.delete(questions.get(0));
        List<Question> findAllQuestionByDeletedFalse = questionRepository.findByDeletedFalse();

        assertThat(findAllQuestionByDeletedFalse.size()).isEqualTo(questions.size() - 1);
    }

    @DisplayName("Question의 title을 변경한다.")
    @Test
    void update_question_title_test() {
        Question question = QuestionTest.Q2;
        Question saveQuestion = questionRepository.save(question);

        saveQuestion.setTitle("테스트1");
        Question findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion.getId()).get();

        assertThat(findQuestion.getTitle()).isEqualTo(saveQuestion.getTitle());
    }
}