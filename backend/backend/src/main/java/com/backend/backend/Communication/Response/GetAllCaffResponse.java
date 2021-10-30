package com.backend.backend.Communication.Response;

import com.backend.backend.Data.Caff;

import java.util.List;

public class GetAllCaffResponse {
    private Caff caff;

    private String username;

    List<GetCommentResponse> comments;

    public Caff getCaff() {
        return caff;
    }

    public void setCaff(Caff caff) {
        this.caff = caff;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<GetCommentResponse> getComments() {
        return comments;
    }

    public GetAllCaffResponse(Caff caff, String username, List<GetCommentResponse> getCommentResponses) {
        this.caff  =caff;
        this.username = username;
        this.comments = getCommentResponses;
    }
}
