package com.Paul.web.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Organisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    @Column(name = "owner_id")
    private int ownerId;

    @JsonIgnore
    @OneToMany(mappedBy = "organisation", cascade = CascadeType.ALL)
    private Set<Group> groups = new HashSet<Group>();

    @JsonIgnore
    @OneToMany(mappedBy="organisation", cascade = CascadeType.DETACH)
    private Set<User> participants = new HashSet<User>();

    public Organisation() {
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

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }
}
