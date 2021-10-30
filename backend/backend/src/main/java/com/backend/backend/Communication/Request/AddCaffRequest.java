package com.backend.backend.Communication.Request;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AddCaffRequest {

    @NotBlank
    @Column(unique=true)
    @Size(max = 50)
    private String title;

    @NotBlank
    private String caffFile;

    public String getTitle() {return this.title;}

    public void setTitle(String title) {this.title = title;}

    public String getCaffFile() {
        return caffFile;
    }

    public void setCaffFile(String caff) {
        this.caffFile = caff;
    }
}
