package stack.overflow.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "error_exception_question")
public class ErrExeQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long questionId;

    @Column(name = "tag")
    private String tag;

    @Column(name = "title")
    private String title;

    @Column(name = "creationdate")
    private Long creationDate;

    @Column(name = "answercount")
    private Integer answerCount;

    @Column(name = "viewcount")
    private Integer viewCount;

    @Column(name = "score")
    private Integer score;

    @Column(name = "exceptionmatches")
    private String exceptionMatches;

    @Column(name = "errormatches")
    private String errorMatches;

    // Constructor

    public ErrExeQuestion() {}

    public ErrExeQuestion(String tag, String title, Long creationDate, Integer answerCount, Integer viewCount, Integer score, String exceptionMatches, String errorMatches) {
        this.tag = tag;
        this.title = title;
        this.creationDate = creationDate;
        this.answerCount = answerCount;
        this.viewCount = viewCount;
        this.score = score;
        this.exceptionMatches = exceptionMatches;
        this.errorMatches = errorMatches;
    }

    // Getters and Setters

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getExceptionMatches() {
        return exceptionMatches;
    }

    public void setExceptionMatches(String ExceptionMatches) {
        this.exceptionMatches = exceptionMatches;
    }

    public String getErrorMatches() {
        return errorMatches;
    }

    public void setErrorMatches(String ErrorMatches) {
        this.errorMatches = errorMatches;
    }
}
