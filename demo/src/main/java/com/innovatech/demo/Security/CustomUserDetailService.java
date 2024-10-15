package com.innovatech.demo.Security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.Role;
import com.innovatech.demo.Entity.UserEntity;
import com.innovatech.demo.Repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTGenerator jwtGenerator;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found"));
        return new User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(List.of(user.getRole())));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public String getUserRoleFromToken(String token) {
        if (jwtGenerator.validateToken(token)) {
            // Extraer el nombre de usuario del token
            String username = jwtGenerator.getUserFromJwt(token);
            
            // Cargar los detalles del usuario desde la base de datos
            UserDetails userDetails = loadUserByUsername(username);

            // Obtener el rol del usuario
            return userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority())
                    .orElse("ROLE_USER"); // Retornar un valor predeterminado si no se encuentra ningún rol
        }
        throw new IllegalArgumentException("Invalid JWT token");
    }
}
