package stack.overflow.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stack.overflow.demo.model.TopTagQuestion;

public interface TopTagQuestionRepository extends JpaRepository<TopTagQuestion, Long> {
}
