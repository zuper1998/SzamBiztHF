package com.backend.backend.Communication.Response;

import com.backend.backend.Data.Caff;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.narcano.jni.CIFF;

public class GetAllCaffResponse {

    private UUID id;

    private CaffDTO caff;

    private String username;

    List<GetCommentResponse> comments;

    public CaffDTO getCaff() {
        return caff;
    }

    public void setCaff(CaffDTO caff) {
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

    public UUID getId() {
        return id;
    }

    public GetAllCaffResponse(UUID id, CIFF[] ciffs, String username, List<GetCommentResponse> getCommentResponses) {
        ArrayList<Ciff> ciff = new ArrayList<>();
        for(int i=0; i<ciffs.length; i++) {
            ciff.add(new Ciff(ciffs[i].width, ciffs[i].height, ciffs[i].duration,ciffs[i].rgb_values));
        }
        this.caff = new CaffDTO(ciff);
        this.id = id;
        this.username = username;
        this.comments = getCommentResponses;
    }
}
