package stack.overflow.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import stack.overflow.demo.service.TopTagQuestionService;
import stack.overflow.demo.model.TopTagQuestion;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
public class TopTagQuestionController {
    private final TopTagQuestionService topTagQuestionService;
    private static final Logger LOGGER = LoggerFactory.getLogger(TopTagQuestionController.class);

    public TopTagQuestionController(TopTagQuestionService topTagQuestionService) {
        LOGGER.info("Create new TopTagQuestionController");
        this.topTagQuestionService = topTagQuestionService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<TopTagQuestion> getTopTags() {
        LOGGER.info("Receive new request for all TopTagsQuestion");
        return topTagQuestionService.getTopTags();
    }


    @RequestMapping(value = "/tagsCount", method = RequestMethod.GET)
    public Map<String, Long> getTagsCount() {
        LOGGER.info("Receive new request for all tags count");

        List<TopTagQuestion> topTagQuestions = topTagQuestionService.getTopTags();
        return topTagQuestions.stream()
                .map(TopTagQuestion::getTags)
                .flatMap(tags -> Arrays.stream(tags.split(",")))
                .collect(Collectors.groupingBy(
                        tag -> tag,
                        Collectors.counting()
                ));

//        Map<String, Integer> tagsCount = new HashMap<>();
//        for (TopTagQuestion topTagQuestion : topTagQuestions) {
//            List<String> currentTags = List.of(topTagQuestion.getTags().split(","));
//            for(String tags : currentTags) {
//                if(tagsCount.containsKey(tags)) {
//                    tagsCount.put(tags, tagsCount.get(tags) + 1);
//                }else {
//                    tagsCount.put(tags, 1);
//                }
//            }
//        }
//        return tagsCount;
    }
}
