package com.Paul.web.app.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import javax.persistence.*;
import java.util.Set;

@Entity
public class AnswerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String answer;

    private boolean isCorrect;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "task")
    private Task task;

    @JsonIgnore
    @OneToMany(mappedBy = "answerOption")
    private Set<StudentAnswer> answers;


    public Set<StudentAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<StudentAnswer> answers) {
        this.answers = answers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @JsonGetter("isCorrect")
    public boolean isCorrect() {
        return isCorrect;
    }

    @JsonSetter("isCorrect")
    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}