package stack.overflow.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "top_tags_question")
public class TopTagQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long questionId;

    @Column(name = "tags")
    private String tags;

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

    // Constructor

    public TopTagQuestion() {}

    public TopTagQuestion(String tags, String tag, String title, Long creationDate, Integer answerCount, Integer viewCount, Integer score) {
        this.tags = tags;
        this.tag = tag;
        this.title = title;
        this.creationDate = creationDate;
        this.answerCount = answerCount;
        this.viewCount = viewCount;
        this.score = score;
    }

    // Getters and Setters

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getTags() {return tags;}

    public void setTags(String tags) {this.tags = tags;}

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
}
