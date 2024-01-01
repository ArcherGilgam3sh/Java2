package stack.overflow.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import stack.overflow.demo.model.ErrExeQuestion;
import stack.overflow.demo.service.ErrExeQuestionService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
public class ErrExeQuestionController {
    private final ErrExeQuestionService errExeQuestionService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrExeQuestionController.class);

    public ErrExeQuestionController(ErrExeQuestionService errExeQuestionService) {
        LOGGER.info("Create new ErrExeQuestionController");
        this.errExeQuestionService = errExeQuestionService;
    }

    // 获取到所有的bug类型的数据
    @RequestMapping(value = "/bug/list", method = RequestMethod.GET)
    public List<ErrExeQuestion> getTopTags() {
        LOGGER.info("Receive new request for all ErrExeQuestion");
        return errExeQuestionService.getErrExe();
    }

    // 获取到所有不同的Error类型
    @RequestMapping(value = "/bug/errAll", method = RequestMethod.GET)
    public List<String> getAllErr() {
        LOGGER.info("Receive new request for all Error");
        return errExeQuestionService.getAllErr();
    }

    // 获取到Error类型前十的数据，不包含单纯的ERROR\error
    @RequestMapping(value = "/bug/errStat", method = RequestMethod.GET)
    public HashMap<String, Long> getErrStat() {
        LOGGER.info("Receive new request for Error Stats");

        List<ErrExeQuestion> errExeQuestions = errExeQuestionService.getErrExe();

        Map<String, Long> errorMap = errExeQuestions.stream()
                .map(ErrExeQuestion::getErrorMatches)
                .filter(errorMatches -> !errorMatches.isEmpty())
                .filter(errorMatches -> !errorMatches.matches(".*\\b(?:error|Error|ERROR|errors)\\b.*"))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return errorMap.entrySet().stream()
                .filter(entry -> !entry.getKey().matches(".*\\b(?:error|Error|ERROR|errors)\\b.*"))
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    // 用户指定单个Error类型，返回其出现的数量
    @RequestMapping(value = "/bug/singleErr", method = RequestMethod.GET)
    public String getSingleErrStat(@RequestParam(value = "errs") String err) {
        LOGGER.info("Receive new request for Error Stats with search parameter: {}", err);

        List<ErrExeQuestion> errExeQuestions = errExeQuestionService.getErrExe();

        Map<String, Long> errorMap = errExeQuestions.stream()
                .map(ErrExeQuestion::getErrorMatches)
                .filter(errorMatches -> !errorMatches.isEmpty())
                .filter(errorMatches -> !errorMatches.matches(".*\\b(?:error|Error|ERROR|errors)\\b.*"))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        if (errorMap.containsKey(err)) {
            Map<String, Long> resultMap = new LinkedHashMap<>();
            resultMap.put(err, errorMap.get(err));

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(resultMap);
            } catch (JsonProcessingException e) {
                e.printStackTrace();

                LOGGER.error("Error processing JSON data");
                return "Error processing JSON data";
            }
        } else {
            LOGGER.error("User request for Error {} but not found", err);
            return "Error not found: " + err;
        }
    }

    @RequestMapping(value = "/bug/listErr", method = RequestMethod.GET)
    public String getListErrStat(@RequestParam(value = "errs") List<String> errs) {
        LOGGER.info("Receive new request for Error Stats with search parameters: {}", errs);

        Map<String, Long> resultMap = new LinkedHashMap<>();

        List<ErrExeQuestion> errExeQuestions = errExeQuestionService.getErrExe();

        // 处理errExeQuestions，生成errorMap
        Map<String, Long> errorMap = errExeQuestions.stream()
                .map(ErrExeQuestion::getErrorMatches)
                .filter(errorMatches -> !errorMatches.isEmpty())
                .filter(errorMatches -> !errorMatches.matches(".*\\b(?:error|Error|ERROR|errors)\\b.*"))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        for (String err : errs) {
            if (errorMap.containsKey(err)) {
                resultMap.put(err, errorMap.get(err));
            } else {
                LOGGER.error("User request for Error {} but not found", err);
                resultMap.put("Error not found: " + err, 0L);
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(resultMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            LOGGER.error("Error processing JSON data");
            return "Error processing JSON data";
        }
    }

    // 获取到所有不同的Exception类型
    @RequestMapping(value = "/bug/exeAll", method = RequestMethod.GET)
    public List<String> getAllExe() {
        LOGGER.info("Receive new request for all Exception");
        return errExeQuestionService.getAllExe();
    }

    // 获取到Exception类型前十的数据，不包含单纯的exception
    @RequestMapping(value = "/bug/exeStat", method = RequestMethod.GET)
    public HashMap<String, Long> getExeStat() {
        LOGGER.info("Receive new request for Exception Stats");

        List<ErrExeQuestion> errExeQuestions = errExeQuestionService.getErrExe();

        Map<String, Long> errorMap = errExeQuestions.stream()
                .map(ErrExeQuestion::getExceptionMatches)
                .filter(exceptionMatches -> !exceptionMatches.isEmpty())
                .filter(exceptionMatches -> !exceptionMatches.matches(".*\\b(?:Exception|exception)\\b.*"))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return errorMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    // 用户指定单个Exception类型，返回其出现的数量
    @RequestMapping(value = "/bug/singleExe", method = RequestMethod.GET)
    public String getSingleExeStat(@RequestParam(value = "exe") String exe) {
        LOGGER.info("Receive new request for Exception Stats with search parameter: {}", exe);

        List<ErrExeQuestion> errExeQuestions = errExeQuestionService.getErrExe();

        Map<String, Long> errorMap = errExeQuestions.stream()
                .map(ErrExeQuestion::getExceptionMatches)
                .filter(exceptionMatches -> !exceptionMatches.isEmpty())
                .filter(exceptionMatches -> !exceptionMatches.matches(".*\\b(?:Exception|exception)\\b.*"))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        if (errorMap.containsKey(exe)) {
            Map<String, Long> resultMap = new LinkedHashMap<>();
            resultMap.put(exe, errorMap.get(exe));

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(resultMap);
            } catch (JsonProcessingException e) {
                e.printStackTrace();

                LOGGER.error("Error processing JSON data");
                return "Exception processing JSON data";
            }
        } else {
            LOGGER.error("User request for Exception {} but not found", exe);
            return "Error not found: " + exe;
        }
    }

    // 用户指定一个列表的Exception，返回各自出现的数量
    @RequestMapping(value = "/bug/listExe", method = RequestMethod.GET)
    public String getListExeStat(@RequestParam(value = "exe") List<String> exes) {
        LOGGER.info("Receive new request for Error Stats with search parameters: {}", exes);

        Map<String, Long> resultMap = new LinkedHashMap<>();

        List<ErrExeQuestion> errExeQuestions = errExeQuestionService.getErrExe();

        // 处理errExeQuestions，生成errorMap
        Map<String, Long> errorMap = errExeQuestions.stream()
                .map(ErrExeQuestion::getExceptionMatches)
                .filter(exceptionMatches -> !exceptionMatches.isEmpty())
                .filter(exceptionMatches -> !exceptionMatches.matches(".*\\b(?:exception|Exception)\\b.*"))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        for (String exe : exes) {
            if (errorMap.containsKey(exe)) {
                resultMap.put(exe, errorMap.get(exe));
            } else {
                LOGGER.error("User request for Exception {} but not found", exe);
                resultMap.put("Exception not found: " + exe, 0L);
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(resultMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            LOGGER.error("Error processing JSON data");
            return "Error processing JSON data";
        }
    }

    // 获取到Error和Exception的总数
    @RequestMapping(value = "/bug/errAndExe", method = RequestMethod.GET)
    public HashMap<String, Long> getErrAndExe() {
        LOGGER.info("Receive new request for Exception and Error Stats");

        List<ErrExeQuestion> errExeQuestions = errExeQuestionService.getErrExe();

        long errorMatchesCount = errExeQuestions.stream()
                .filter(question -> !question.getErrorMatches().isEmpty())
                .count();

        long exceptionMatchesCount = errExeQuestions.stream()
                .filter(question -> !question.getExceptionMatches().isEmpty())
                .count();

        HashMap<String, Long> resultMap = new HashMap<>();
        resultMap.put("errorMatchesCount", errorMatchesCount);
        resultMap.put("exceptionMatchesCount", exceptionMatchesCount);

        return resultMap;
    }
}
