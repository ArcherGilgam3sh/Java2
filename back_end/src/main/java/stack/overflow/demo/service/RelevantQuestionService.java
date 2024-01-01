package stack.overflow.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stack.overflow.demo.model.RelevantQuestion;
import stack.overflow.demo.repository.RelevantQuestionRepository;

import java.util.List;

@Component
public class RelevantQuestionService {
    private final RelevantQuestionRepository relevantQuestionRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(RelevantQuestionService.class);

    @Autowired
    public RelevantQuestionService(RelevantQuestionRepository relevantQuestionRepository) {
        LOGGER.info("Create new RelevantQuestionService");
        this.relevantQuestionRepository = relevantQuestionRepository;
    }

    public List<RelevantQuestion> getRelTags() {
        return relevantQuestionRepository.findAll();
    }

    public List<RelevantQuestion> getQuestionsByTag(String tag) {
        String tagComma = "%" + tag + ",%";
        String tagSpace = "%" + tag + " %";
        String tagAlone = tag;
        String commaTag = "%," + tag;

        return relevantQuestionRepository.findByCustomTagMatching(tagComma, tagSpace, tagAlone, commaTag);
    }

}
