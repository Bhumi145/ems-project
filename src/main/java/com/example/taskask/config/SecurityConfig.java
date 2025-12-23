package com.example.taskask.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.taskask.security.CustomUserDetailsService;
import com.example.taskask.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Value("#{'${app.cors.allowed-origins:http://localhost:4200,http://localhost:64561,http://localhost:63591}'.split(',')}")
    private List<String> allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/health", "/actuator/health").permitAll()
                // Role & Permission APIs
                .requestMatchers("/api/roles/**", "/api/permissions/**").hasRole("ADMIN")
                // Employees: allow create/list/get for Admin/Manager
                .requestMatchers(HttpMethod.POST, "/api/employees").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.GET, "/api/employees/**").hasAnyRole("ADMIN", "MANAGER")
                // Task history (viewable by all authenticated users)
                .requestMatchers("/api/tasks/*/history/**").authenticated()
                // Audit logs (admin only)
                .requestMatchers("/api/audit-logs/**").hasRole("ADMIN")
                // Notifications (user can only access their own)
                .requestMatchers("/api/notifications/user/*").authenticated()
                // KPI APIs (manager, admin, HR)
                .requestMatchers("/api/kpi/**").hasAnyRole("ADMIN", "MANAGER", "HR")
                // Default: all other endpoints require authentication
                .anyRequest().authenticated()
            )
            .userDetailsService(customUserDetailsService)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        List<String> origins = allowedOrigins == null || allowedOrigins.isEmpty()
                ? List.of("http://localhost:4200")
                : allowedOrigins.stream()
                        .map(String::trim)
                        .filter(origin -> !origin.isEmpty())
                        .toList();

        configuration.setAllowedOrigins(origins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
