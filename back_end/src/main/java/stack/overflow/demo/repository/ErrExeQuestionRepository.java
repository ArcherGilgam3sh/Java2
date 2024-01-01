package stack.overflow.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import stack.overflow.demo.model.ErrExeQuestion;

import java.util.List;

public interface ErrExeQuestionRepository extends JpaRepository<ErrExeQuestion, Long> {

    @Query("SELECT DISTINCT errorMatches FROM ErrExeQuestion")
    List<String> findAllErr();

    @Query("SELECT DISTINCT exceptionMatches FROM ErrExeQuestion")
    List<String> findAllExe();
}
