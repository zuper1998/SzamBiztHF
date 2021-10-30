package com.backend.backend.Communication.Request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SearchRequest {
    @NotNull
    @NotBlank
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
