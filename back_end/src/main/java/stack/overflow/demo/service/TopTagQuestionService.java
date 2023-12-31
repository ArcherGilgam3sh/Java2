package stack.overflow.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stack.overflow.demo.repository.TopTagQuestionRepository;
import stack.overflow.demo.model.TopTagQuestion;

import java.util.List;

@Service
public class TopTagQuestionService {

    private final TopTagQuestionRepository topTagQuestionRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(TopTagQuestionService.class);

    @Autowired
    public TopTagQuestionService(TopTagQuestionRepository topTagQuestionRepository) {
        LOGGER.info("Create new TopTagQuestionService");
        this.topTagQuestionRepository = topTagQuestionRepository;
    }

    public List<TopTagQuestion> getTopTags() {
        return topTagQuestionRepository.findAll();
    }

    public List<Object[]> getTagsStats() {
        return topTagQuestionRepository.findAverageStatsPerTag();
    }
}
