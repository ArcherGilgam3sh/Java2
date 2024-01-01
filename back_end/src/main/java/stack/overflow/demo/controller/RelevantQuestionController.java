package stack.overflow.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import stack.overflow.demo.model.RelevantQuestion;
import stack.overflow.demo.service.RelevantQuestionService;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
public class RelevantQuestionController {
    private final RelevantQuestionService relevantQuestionService;
    private static final Logger LOGGER = LoggerFactory.getLogger(RelevantQuestionController.class);

    public RelevantQuestionController(RelevantQuestionService relevantQuestionService) {
        LOGGER.info("Create new RelevantQuestionController");
        this.relevantQuestionService = relevantQuestionService;
    }

    // 获取到Relevant Question所有的数据
    @RequestMapping(value = "/rel/list", method = RequestMethod.GET)
    public List<RelevantQuestion> getRelTags() {
        LOGGER.info("Receive new request for all relevant tags");
        return relevantQuestionService.getRelTags();
    }

    @RequestMapping(value = "/rel/get", method = RequestMethod.GET)
    public String getRelevantTags(@RequestParam(value = "tag") String tag) {
        LOGGER.info("Receive new request for tags relevant to {}", tag);

        List<RelevantQuestion> relevantQuestions = relevantQuestionService.getQuestionsByTag(tag);
        HashMap<String, Integer> resultMap = new HashMap<>();

        for (RelevantQuestion relevantQuestion : relevantQuestions) {
            String[] currentTags = relevantQuestion.getTags().split(",");
            for (String currentTag : currentTags) {
                resultMap.put(currentTag, resultMap.getOrDefault(currentTag, 0) + 1);
            }
        }

        Map<String, Integer> sortedMap = resultMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(sortedMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            LOGGER.error("Error processing JSON data");
            return "Error processing JSON data";
        }
    }
}
