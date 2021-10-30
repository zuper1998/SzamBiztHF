package com.backend.backend.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Entity
public class Caff {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    @Column(unique=true)
    @Size(max = 50)
    private String title;

    @NotBlank
    private String caffFile;

    @OneToMany(mappedBy = "caffPost")
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Caff(String title, String caffFile, List<Comment> comments, User user) {
        this.title = title;
        this.caffFile = caffFile;
        this.comments = comments;
        this.user = user;
    }

    public Caff() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {return this.title;}

    public void setTitle(String title) {this.title = title;}

    public String getCaffFile() {
        return caffFile;
    }

    public void setCaffFile(String caff) {
        this.caffFile = caff;
    }

    @JsonIgnore
    public List<Comment> getComments() {
        return this.comments;
    }

    @JsonIgnore
    public User getUser() {return this.user;}

    public void setUser(User user) {this.user = user;}
}
