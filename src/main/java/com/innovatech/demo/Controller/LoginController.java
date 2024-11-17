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
import com.innovatech.demo.Service.UserService;
import com.innovatech.demo.Security.JWTGenerator;
import com.innovatech.demo.Security.CustomUserDetailService;

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

    // Clave secreta para el desencriptado
    private static final String SECRET_KEY = "mySecretKey12345";  // Debe coincidir con la clave secreta del frontend

    @PostMapping()
    public ResponseEntity<?> login(@RequestBody UserDTO user) {
        try {
            // Desencriptar la contraseña recibida desde el frontend usando XOR
            String decryptedPassword = decrypt(user.getPassword());

            // Autenticar al usuario con la contraseña desencriptada
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), decryptedPassword));

            // Establecer la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generar el JWT Token
            String token = jwtGenerator.generateToken(authentication);

            // Verificar si el usuario existe en la base de datos
            if (userService.findByEmail(user.getEmail()) == null) {
                return new ResponseEntity<>("Authentication failed", HttpStatus.FORBIDDEN);
            }

            // Devolver el JWT generado
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (AuthenticationException e) {
            // Manejar fallo de autenticación
            return new ResponseEntity<>("Authentication failed", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            // Manejar otros errores
            return new ResponseEntity<>("Error occurred during login", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET: Obtener el rol del usuario a partir del JWT
    @GetMapping("/role")
    public ResponseEntity<String> getRoleFromToken(@RequestHeader("Authorization") String token) {
        try {
            // Extraer el token JWT del encabezado Authorization
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

            // Obtener el rol desde el token
            String role = customUserDetailService.getUserRoleFromToken(jwtToken);

            // Devolver el rol
            return ResponseEntity.ok(role);
        } catch (IllegalArgumentException e) {
            // Manejar token JWT inválido
            return ResponseEntity.badRequest().body("Invalid JWT token");
        } catch (Exception e) {
            // Manejar otros errores
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    // Función para desencriptar la contraseña usando XOR
    private String decrypt(String encryptedPassword) {
        String decrypted = "";
        // Decodificamos la contraseña cifrada de base64 a texto
        String encrypted = new String(java.util.Base64.getDecoder().decode(encryptedPassword));
        
        // Desencriptamos la contraseña utilizando XOR
        for (int i = 0; i < encrypted.length(); i++) {
            decrypted += (char) (encrypted.charAt(i) ^ SECRET_KEY.charAt(i % SECRET_KEY.length()));
        }

        return decrypted;
    }
}
