package stack.overflow.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import stack.overflow.demo.service.TopTagQuestionService;
import stack.overflow.demo.model.TopTagQuestion;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*",maxAge = 3600)
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
    }

    @RequestMapping(value = "/tagStat", method = RequestMethod.GET)
    public String getTagStat() {
        LOGGER.info("Receive new request for all tags stats");

        List<TagStatistics> tagStatisticsList = getTagStatistics();

        Map<String, Object> jsonData = new HashMap<>();
        List<String> tags = tagStatisticsList.stream().map(TagStatistics::getTag).toList();
        List<Double> avgAnswerCounts = tagStatisticsList.stream().map(TagStatistics::getAvgAnswerCount).toList();
        List<Double> avgViewCounts = tagStatisticsList.stream().map(TagStatistics::getAvgViewCount).toList();
        List<Double> avgScores = tagStatisticsList.stream().map(TagStatistics::getAvgScore).toList();

        jsonData.put("data", Map.of(
                "tags", tags,
                "avgAnswerCount", avgAnswerCounts,
                "avgViewCount", avgViewCounts,
                "avgScore", avgScores
        ));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResult = "";
        try {
            jsonResult = objectMapper.writeValueAsString(jsonData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonResult;
    }
    private List<TagStatistics> getTagStatistics() {
        List<Object[]> tagsStats = topTagQuestionService.getTagsStats();
        List<TagStatistics> tagStatisticsList = new ArrayList<>();
        for (Object[] tagStat : tagsStats) {
            String tag = (String) tagStat[0];
            Double avgAnswerCount = (Double) tagStat[1];
            Double avgViewCount = (Double) tagStat[2];
            Double avgScore = (Double) tagStat[3];

            TagStatistics tagStatistics = new TagStatistics();
            tagStatistics.setTag(tag);
            tagStatistics.setAvgAnswerCount(avgAnswerCount);
            tagStatistics.setAvgViewCount(avgViewCount);
            tagStatistics.setAvgScore(avgScore);

            tagStatisticsList.add(tagStatistics);
        }
        return tagStatisticsList;
    }
    private class TagStatistics {
        private String tag;
        private Double avgAnswerCount;
        private Double avgViewCount;
        private Double avgScore;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public Double getAvgAnswerCount() {
            return avgAnswerCount;
        }

        public void setAvgAnswerCount(Double avgAnswerCount) {
            this.avgAnswerCount = avgAnswerCount;
        }

        public Double getAvgViewCount() {
            return avgViewCount;
        }

        public void setAvgViewCount(Double avgViewCount) {
            this.avgViewCount = avgViewCount;
        }

        public Double getAvgScore() {
            return avgScore;
        }

        public void setAvgScore(Double avgScore) {
            this.avgScore = avgScore;
        }
    }
}
