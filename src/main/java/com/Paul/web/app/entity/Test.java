package com.Paul.web.app.entity;

import com.Paul.web.app.entity.serializer.TaskSerializer;
import com.Paul.web.app.entity.serializer.UserSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToOne
    @JsonSerialize(using = UserSerializer.class)
    @JoinColumn(name = "manager_id")
    private User managerId;

    private int maxAttempts;

    private int organisationId;

    @JsonSerialize(contentUsing = TaskSerializer.class)
    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
    private Set<Task> tasks;

    @JsonIgnore
    @OneToMany(mappedBy = "test")
    private Set<TestAttempt> testAttempts;

    public Test() {
    }

    public Set<TestAttempt> getTestAttempts() {
        return testAttempts;
    }

    public void setTestAttempts(Set<TestAttempt> testAttempts) {
        this.testAttempts = testAttempts;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public int getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(int organisationId) {
        this.organisationId = organisationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getManagerId() {
        return managerId;
    }

    public void setManagerId(User managerId) {
        this.managerId = managerId;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }
}