package com.example.UserLocation.Location.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(InMemoryUserDetailsManager inMemoryUserDetailsManager, PasswordEncoder passwordEncoder) {
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = inMemoryUserDetailsManager.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Username not found");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        // Check if the user has the required role
/*        List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return new UsernamePasswordAuthenticationToken(username, password, authorities);
            }
        }*/

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Iterator<GrantedAuthority> iterator = (Iterator<GrantedAuthority>) userDetails.getAuthorities().iterator(); iterator.hasNext(); ) {
            GrantedAuthority authority = iterator.next();
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return new UsernamePasswordAuthenticationToken(username, password, authorities);
            }else if (authority.getAuthority().equals("ROLE_READER")) {
                return new UsernamePasswordAuthenticationToken(username, password, authorities);
            }else{

            }
        }

        // The user does not have the required role
        throw new AccessDeniedException("User does not have the required role");
    }



    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}

