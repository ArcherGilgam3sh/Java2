package stack.overflow.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
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

    // 获取到Top Tags Question所有的数据
    @RequestMapping(value = "/top/list", method = RequestMethod.GET)
    public List<TopTagQuestion> getTopTags() {
        LOGGER.info("Receive new request for all TopTagsQuestion");
        return topTagQuestionService.getTopTags();
    }

    // 获取到所有tags对应的数量，按照tags的字母表进行排序
    @RequestMapping(value = "/top/tagsCount", method = RequestMethod.GET)
    public Map<String, Long> getTagsCount() {
        LOGGER.info("Receive new request for all tags count");

        List<TopTagQuestion> topTagQuestions = topTagQuestionService.getTopTags();
        Map<String, Long> sortedMap = topTagQuestions.stream()
                .map(TopTagQuestion::getTags)
                .flatMap(tags -> Arrays.stream(tags.split(",")))
                .collect(Collectors.groupingBy(
                        tag -> tag,
                        Collectors.counting()
                ));

        return sortedMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    // 获取到所有Tag的热点数据，包含三个指标，回答数、浏览量和评分
    @RequestMapping(value = "/top/tagStat", method = RequestMethod.GET)
    public String getTagStat() {
        LOGGER.info("Receive new request for all tags stats");

        List<TagStatistics> tagStatisticsList = getTagStatistics();

        Map<String, Object> jsonData = new HashMap<>();
        List<String> tags = tagStatisticsList.stream()
                .map(TagStatistics::getTag)
                .sorted() // 对标签名称进行排序
                .toList();
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
            LOGGER.error("Error processing JSON data");
            return "Error processing JSON data";
        }

        return jsonResult;
    }

    // 获取到单个Tag的热点数据
    @RequestMapping(value = "/top/singleTagStat", method = RequestMethod.GET)
    public String getSingleTagStat(@RequestParam(value = "tag") String tag) {
        LOGGER.info("Receive new request for tag stats: {}", tag);

        List<TagStatistics> tagStatisticsList = getTagStatistics();

        Optional<TagStatistics> matchingTagStat = tagStatisticsList.stream()
                .filter(stat -> stat.getTag().equals(tag))
                .findFirst();

        if (matchingTagStat.isPresent()) {
            TagStatistics tagStat = matchingTagStat.get();

            Map<String, Object> jsonData = new HashMap<>();
            jsonData.put("tag", tagStat.getTag());
            jsonData.put("avgAnswerCount", tagStat.getAvgAnswerCount());
            jsonData.put("avgViewCount", tagStat.getAvgViewCount());
            jsonData.put("avgScore", tagStat.getAvgScore());

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(jsonData);
            } catch (JsonProcessingException e) {
                e.printStackTrace();

                LOGGER.error("Error processing JSON data");
                return "Error processing JSON data";
            }
        } else {
            LOGGER.error("User request for tag {} but not found", tag);
            return "Tag not found: " + tag;
        }
    }

    // 获取到一个列表的Tag数据
    @RequestMapping(value = "/top/listTagStat", method = RequestMethod.GET)
    public String getListTagStat(@RequestParam(value = "tags") List<String> tags) {
        LOGGER.info("Receive new request for tags stats: {}", tags);

        List<TagStatistics> tagStatisticsList = getTagStatistics();

        Map<String, Map<String, Object>> tagStatsMap = new HashMap<>();

        for (String tag : tags) {
            Optional<TagStatistics> matchingTagStat = tagStatisticsList.stream()
                    .filter(stat -> stat.getTag().equals(tag))
                    .findFirst();

            Map<String, Object> tagData = new HashMap<>();

            if (matchingTagStat.isPresent()) {
                TagStatistics tagStat = matchingTagStat.get();
                tagData.put("avgAnswerCount", tagStat.getAvgAnswerCount());
                tagData.put("avgViewCount", tagStat.getAvgViewCount());
                tagData.put("avgScore", tagStat.getAvgScore());
            } else {
                tagData.put("error", "Tag not found");
                LOGGER.error("User request for tag {} but not found", tag);
            }

            tagStatsMap.put(tag, tagData);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(tagStatsMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error processing JSON data";
        }
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
    private static class TagStatistics {
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
