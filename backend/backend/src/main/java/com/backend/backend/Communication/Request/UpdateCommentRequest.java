package com.backend.backend.Communication.Request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UpdateCommentRequest {
    @NotNull
    @NotBlank
    private String Text;

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }
}
