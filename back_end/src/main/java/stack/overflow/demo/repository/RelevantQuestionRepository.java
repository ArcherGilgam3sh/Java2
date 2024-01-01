package stack.overflow.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stack.overflow.demo.model.RelevantQuestion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RelevantQuestionRepository extends JpaRepository<RelevantQuestion, Long> {
    @Query("SELECT rq FROM RelevantQuestion rq WHERE rq.tags LIKE %:tagComma% OR rq.tags LIKE %:tagSpace% OR rq.tags = :tagAlone OR rq.tags LIKE :commaTag")
    List<RelevantQuestion> findByCustomTagMatching(@Param("tagComma") String tagComma,
                                                   @Param("tagSpace") String tagSpace,
                                                   @Param("tagAlone") String tagAlone,
                                                   @Param("commaTag") String commaTag);
}
