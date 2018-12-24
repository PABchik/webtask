package com.Paul.web.app.entity;

import com.Paul.web.app.entity.serializer.TestSerializer;
import com.Paul.web.app.entity.serializer.UserSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Set;

@Entity
public class TestAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int attemptNumber;

    @ManyToOne
    @JsonSerialize(using = UserSerializer.class)
    @JoinColumn(name = "student")
    private User student;

    @ManyToOne
    @JsonSerialize(using = TestSerializer.class)
    @JoinColumn(name = "test")
    private Test test;

    @OneToMany(mappedBy = "attempt")
    private Set<StudentAnswer> answers;


    public Set<StudentAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<StudentAnswer> answers) {
        this.answers = answers;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(int attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public TestAttempt() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

