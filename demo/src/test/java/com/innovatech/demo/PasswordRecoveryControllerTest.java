package com.innovatech.demo;


import com.innovatech.demo.Controller.PasswordRecoveryController;
import com.innovatech.demo.DTO.PasswordChangeDTO;
import com.innovatech.demo.DTO.PasswordRecoveryCodeDTO;
import com.innovatech.demo.DTO.PasswordRecoveryEmailDTO;
import com.innovatech.demo.Entity.UserEntity;
import com.innovatech.demo.Service.EmailService;
import com.innovatech.demo.Service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class PasswordRecoveryControllerTest {

    @InjectMocks
    private PasswordRecoveryController passwordRecoveryController;

    @Mock
    private UserService userService;

    @Mock
    private EmailService emailService;

    private Map<String, String> recoveryCodes;
    private Map<String, Boolean> verifiedEmails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        recoveryCodes = new HashMap<>();
        verifiedEmails = new HashMap<>();
    }

    @Test
    public void testRequestPasswordRecovery_UserFound() {
        PasswordRecoveryEmailDTO emailDTO = new PasswordRecoveryEmailDTO();
        emailDTO.setEmail("test@example.com");

        UserEntity user = new UserEntity();
        user.setEmail(emailDTO.getEmail());

        Mockito.when(userService.findByEmail(emailDTO.getEmail())).thenReturn(user);

        ResponseEntity<String> response = passwordRecoveryController.requestPasswordRecovery(emailDTO);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Recovery code sent to email.", response.getBody());
    }

    @Test
    public void testRequestPasswordRecovery_UserNotFound() {
        PasswordRecoveryEmailDTO emailDTO = new PasswordRecoveryEmailDTO();
        emailDTO.setEmail("unknown@example.com");

        Mockito.when(userService.findByEmail(emailDTO.getEmail())).thenReturn(null);

        ResponseEntity<String> response = passwordRecoveryController.requestPasswordRecovery(emailDTO);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("User not found.", response.getBody());
    }

    @Test
    public void testVerifyRecoveryCode_Success() {
        String email = "test@example.com";
        recoveryCodes.put(email, "123456");

        PasswordRecoveryCodeDTO codeDTO = new PasswordRecoveryCodeDTO();
        codeDTO.setCode("123456");

        ResponseEntity<String> response = passwordRecoveryController.verifyRecoveryCode(codeDTO);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Code verified. Proceed to set a new password.", response.getBody());
    }

    @Test
    public void testVerifyRecoveryCode_InvalidCode() {
        PasswordRecoveryCodeDTO codeDTO = new PasswordRecoveryCodeDTO();
        codeDTO.setCode("wrongCode");

        ResponseEntity<String> response = passwordRecoveryController.verifyRecoveryCode(codeDTO);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals("Invalid recovery code.", response.getBody());
    }

    @Test
    public void testSetNewPassword_Success() {
        String email = "test@example.com";
        recoveryCodes.put(email, "123456");
        verifiedEmails.put(email, true);

        PasswordChangeDTO passwordDTO = new PasswordChangeDTO();
        passwordDTO.setNewPassword("newPassword123");
        passwordDTO.setConfirmNewPassword("newPassword123");

        UserEntity user = new UserEntity();
        user.setEmail(email);
        Mockito.when(userService.findByEmail(email)).thenReturn(user);

        ResponseEntity<String> response = passwordRecoveryController.setNewPassword(passwordDTO);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Password updated successfully.", response.getBody());
    }

    @Test
    public void testSetNewPassword_PasswordsDoNotMatch() {
        PasswordChangeDTO passwordDTO = new PasswordChangeDTO();
        passwordDTO.setNewPassword("newPassword123");
        passwordDTO.setConfirmNewPassword("differentPassword");

        ResponseEntity<String> response = passwordRecoveryController.setNewPassword(passwordDTO);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Passwords do not match.", response.getBody());
    }

    @Test
    public void testSetNewPassword_OTPNotValidated() {
        PasswordChangeDTO passwordDTO = new PasswordChangeDTO();
        passwordDTO.setNewPassword("newPassword123");
        passwordDTO.setConfirmNewPassword("newPassword123");

        ResponseEntity<String> response = passwordRecoveryController.setNewPassword(passwordDTO);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals("OTP not validated.", response.getBody());
    }
}
