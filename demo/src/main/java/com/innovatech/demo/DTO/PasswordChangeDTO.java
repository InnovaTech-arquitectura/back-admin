package com.innovatech.demo.DTO; 

public class PasswordChangeDTO {
    private String newPassword;
    private String confirmNewPassword;

    // Getters y Setters

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
