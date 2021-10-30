package com.backend.backend.Communication.Response;

import java.util.UUID;

public class GetCommentResponse {

    private UUID id;

    private String text;

    private String username;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public GetCommentResponse(UUID id, String text, String username) {
        this.id = id;
        this.text = text;
        this.username = username;
    }
}
