package com.innovatech.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innovatech.demo.DTO.UserDTO;
import com.innovatech.demo.Security.CustomUserDetailService;
import com.innovatech.demo.Security.JWTGenerator;
import com.innovatech.demo.Service.UserService;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.util.Base64;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    // Clave secreta (debería ser la misma que la del frontend, aunque en producción es recomendable usar una estrategia más segura)
    private static final String SECRET_KEY = "mySecretKey12345";  // Asegúrate de tener una clave de longitud adecuada
    
    @PostMapping()
    public ResponseEntity<?> login(@RequestBody UserDTO user) {
        try {
            // Desciframos la contraseña que llega cifrada en base64
            String decryptedPassword = decryptPassword(user.getPassword());

            // Autenticamos al usuario con la contraseña descifrada
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), decryptedPassword));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);

            if (userService.findByEmail(user.getEmail()) == null) {
                return new ResponseEntity<>("Authentication failed", HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Authentication failed", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing password", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/role")
    public ResponseEntity<String> getRoleFromToken(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            String role = customUserDetailService.getUserRoleFromToken(jwtToken);
            return ResponseEntity.ok(role);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid JWT token");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    // Función para descifrar la contraseña usando AES-GCM
    private String decryptPassword(String encryptedPassword) throws Exception {
        // El formato es "ivBase64:cipherTextBase64"
        String[] parts = encryptedPassword.split(":");
        String ivBase64 = parts[0];
        String cipherTextBase64 = parts[1];

        // Convertir las cadenas base64 a byte[]
        byte[] iv = Base64.getDecoder().decode(ivBase64);
        byte[] cipherText = Base64.getDecoder().decode(cipherTextBase64);

        // Crear la clave secreta desde el mismo valor que se usa en el frontend
        byte[] keyBytes = SECRET_KEY.getBytes("UTF-8");

        // Crear el objeto Key para el algoritmo AES
        SecretKey key = new javax.crypto.spec.SecretKeySpec(keyBytes, "AES");

        // Inicializar el descifrador con el algoritmo AES-GCM y el IV
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);  // 128-bit authentication tag
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        // Realizar el descifrado
        byte[] original = cipher.doFinal(cipherText);

        // Convertir el resultado en un string (asumimos que la contraseña es un texto plano en UTF-8)
        return new String(original, "UTF-8");
    }
}
