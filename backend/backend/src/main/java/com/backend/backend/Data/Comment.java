package com.backend.backend.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
public class Comment {

    @Id
    @GeneratedValue
    @Type(type="uuid-char")
    private UUID id;

    @NotBlank
    private String text;

    @ManyToOne
    @JoinColumn(name = "caff_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Caff caffPost;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Comment(String text, Caff caffPost, User user) {
        this.text = text;
        this.caffPost = caffPost;
        this.user = user;
    }

    public Comment() {

    }

    public Comment(String text) {
        this.text = text;
    }

    public UUID getId() { return id; }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JsonIgnore
    public Caff getCaffPost() {return this.caffPost;}

    public void setCaffPost(Caff caffPost) {
        this.caffPost = caffPost;
    }

    @JsonIgnore
    public User getUser() {return this.user;}

    public void setUser(User user) {this.user = user;}
}
