package com.backend.backend.Communication.Request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UpdateUserRequest {

    @NotNull
    @NotBlank
    private String email;

    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email= email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
