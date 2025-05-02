package com.diploma.model.request;

import jakarta.validation.constraints.NotEmpty;

public class RegistrationRequest {
    @NotEmpty(message = "The name is required.")
    private String name;

    @NotEmpty(message = "The email is required.")
    private String email;

    @NotEmpty(message = "The password is required.")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
