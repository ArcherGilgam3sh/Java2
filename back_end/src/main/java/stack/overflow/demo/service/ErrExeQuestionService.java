package stack.overflow.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stack.overflow.demo.model.ErrExeQuestion;
import stack.overflow.demo.repository.ErrExeQuestionRepository;

import java.util.List;

@Component
public class ErrExeQuestionService {
    private final ErrExeQuestionRepository errExeQuestionRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrExeQuestionService.class);

    @Autowired
    public ErrExeQuestionService(ErrExeQuestionRepository errExeQuestionRepository) {
        LOGGER.info("Create new ErrExeQuestionService");
        this.errExeQuestionRepository = errExeQuestionRepository;
    }

    public List<ErrExeQuestion> getErrExe() {
        return errExeQuestionRepository.findAll();
    }

    public List<String> getAllErr() { return errExeQuestionRepository.findAllErr(); }

    public List<String> getAllExe() {return errExeQuestionRepository.findAllExe(); }
}
