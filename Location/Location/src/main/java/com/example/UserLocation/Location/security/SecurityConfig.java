package com.example.UserLocation.Location.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public CustomAuthenticationProvider authenticationProvider(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        return new CustomAuthenticationProvider(inMemoryUserDetailsManager,passwordEncoder());
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.withUsername("Ayush")
                        .password(passwordEncoder().encode("password"))
                        .roles("ADMIN")
                        .build(),
                User.withUsername("Ayush1")
                        .password(passwordEncoder().encode("password"))
                        .roles("READER")
                        .build()
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(httpSecurityCsrfConfigurer -> {
            try {
                httpSecurityCsrfConfigurer.disable()
                        .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                            try {
                                authorizationManagerRequestMatcherRegistry.
                                        requestMatchers( "/get_users/**").hasRole("READER")
                                        .requestMatchers("create_data", "update_data", "delete_data/**").hasRole("ADMIN")
                                        .anyRequest().authenticated();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }).formLogin(Customizer.withDefaults());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return http.build() ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}


