package com.innovatech.demo.Controller;

import com.innovatech.demo.Service.UserService;
import com.innovatech.demo.Service.EmailService;
import com.innovatech.demo.DTO.PasswordRecoveryEmailDTO;
import com.innovatech.demo.DTO.PasswordRecoveryCodeDTO;
import com.innovatech.demo.DTO.PasswordChangeDTO;
import com.innovatech.demo.Entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/password-recovery")
public class PasswordRecoveryController {

    private Map<String, String> recoveryCodes = new HashMap<>();
    private Map<String, Boolean> verifiedEmails = new HashMap<>();

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    // Solicitar código de recuperación
    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordRecovery(@RequestBody PasswordRecoveryEmailDTO emailDTO) {
        UserEntity user = userService.findByEmail(emailDTO.getEmail());
        if (user != null) {
            String recoveryCode = UUID.randomUUID().toString().substring(0, 6);
            recoveryCodes.put(emailDTO.getEmail(), recoveryCode);
            emailService.sendRecoveryCode(emailDTO.getEmail(), recoveryCode);
            return ResponseEntity.ok("Recovery code sent to email.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    // Verificar código de recuperación
    @PostMapping("/verify")
    public ResponseEntity<String> verifyRecoveryCode(@RequestBody PasswordRecoveryCodeDTO codeDTO) {
        String email = codeDTO.getEmail();
        String code = codeDTO.getCode();
        
        if (recoveryCodes.containsKey(email) && recoveryCodes.get(email).equals(code)) {
            // Mark the email as verified for password change
            verifiedEmails.put(email, true);
            return ResponseEntity.ok("Code verified. Proceed to set a new password.");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid recovery code.");
    }

    // Establecer nueva contraseña
    @PostMapping("/set-password")
    public ResponseEntity<String> setNewPassword(@RequestBody PasswordChangeDTO passwordDTO) {
        // Check if the OTP was verified before allowing password change
        if (!verifiedEmails.containsKey(passwordDTO.getEmail()) || !verifiedEmails.get(passwordDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OTP not validated.");
        }
        
        // Validar que las contraseñas coincidan
        if (passwordDTO.getNewPassword().equals(passwordDTO.getConfirmNewPassword())) {
            UserEntity user = userService.findByEmail(passwordDTO.getEmail());
            if (user != null) {
                // Actualizar la contraseña
                user.setPassword(passwordDTO.getNewPassword());
                
                // Guardar el usuario con la nueva contraseña
                userService.save(user);

                // Limpiar la marca de verificación y el código OTP después de actualizar la contraseña
                verifiedEmails.remove(passwordDTO.getEmail());
                recoveryCodes.remove(passwordDTO.getEmail());

                return ResponseEntity.ok("Password updated successfully.");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match.");
    }
}
