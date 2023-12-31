package stack.overflow.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stack.overflow.demo.model.TopTagQuestion;

import java.util.List;

public interface TopTagQuestionRepository extends JpaRepository<TopTagQuestion, Long> {

    @Query("SELECT DISTINCT tag FROM TopTagQuestion")
    List<String> findAllTags();

    @Query("SELECT AVG(answerCount) FROM TopTagQuestion WHERE tag = :tag")
    Double findAverageAnswerCountByTag(@Param("tag") String tag);

    @Query("SELECT AVG(viewCount) FROM TopTagQuestion WHERE tag = :tag")
    Double findAverageViewCountByTag(@Param("tag") String tag);

    @Query("SELECT AVG(score) FROM TopTagQuestion WHERE tag = :tag")
    Double findAverageScoreByTag(@Param("tag") String tag);

    @Query("SELECT tag, AVG(answerCount) AS avgAnswerCount, AVG(viewCount) AS avgViewCount, AVG(score) AS avgScore " +
            "FROM TopTagQuestion " +
            "GROUP BY tag")
    List<Object[]> findAverageStatsPerTag();
}
