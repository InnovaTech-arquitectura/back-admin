package com.innovatech.demo.DTO;

public class PasswordRecoveryCodeDTO {
    private String email; // Email of the user
    private String code;  // Recovery code

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
