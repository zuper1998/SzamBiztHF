package com.backend.backend.Communication.Request;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.File;

public class AddCaffRequest {

    @NotBlank
    @Column(unique=true)
    @Size(max = 50)
    private String title;

    public String getTitle() {return this.title;}

    public void setTitle(String title) {this.title = title;}
}
