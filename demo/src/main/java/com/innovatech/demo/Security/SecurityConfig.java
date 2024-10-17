package com.innovatech.demo.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JWTAuthEntryPoint jwtAuthEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .csrf(AbstractHttpConfigurer::disable) 
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(customizer -> customizer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/login/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/event/**")).hasAnyAuthority("Administrator", "Entrepreneurship")
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/plan/**")).hasAnyAuthority("Administrator", "Sales", "Billing", "Marketing")
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/course/**")).hasAnyAuthority("Administrator", "Specialist")
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/banner/**")).hasAnyAuthority("Administrator", "Marketing")
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/profile/**")).hasAuthority("Administrator")
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/dashboard/**")).hasAnyAuthority("Administrator", "Entrepreneurship", "Specialist", "Sales", "Billing", "Marketing")
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/finance/**")).hasAnyAuthority("Administrator", "Sales", "Billing", "Marketing")
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/password-recovery/request")).permitAll()
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/api/password-recovery/verify")).permitAll()
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/api/password-recovery/set-password")).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint));

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }

}
