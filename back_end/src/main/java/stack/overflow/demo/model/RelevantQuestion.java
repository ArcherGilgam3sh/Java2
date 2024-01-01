package stack.overflow.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "relevant_tag_data")
public class RelevantQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long questionId;

    @Column(name = "tags")
    private String tags;

    // Constructor

    public RelevantQuestion() {}

    public RelevantQuestion(String tags) {
        this.tags = tags;
    }

    // Getters and Setters

    public Long getId() {
        return questionId;
    }

    public void setId(Long questionId) {
        this.questionId = questionId;
    }

    public String getTags() {return tags;}

    public void setTags(String tags) {this.tags = tags;}
}
