package com.obviousai.inventoryms;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class Config {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests(auth -> auth
//                .antMatchers("/payment/**").authenticated() // Require authentication for payment endpoints
//                .antMatchers("/inventory/**")//.hasRole("ADMIN") // Require ADMIN role for inventory endpoints
                .anyRequest().permitAll() // Allow all other requests
            );

        return http.build();
    }

    /*
    // Optional: Include this method if you have paths to ignore
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/ignore1", "/ignore2"); // Customize ignored paths if needed
    }
    */
    @Bean
    public ModelMapper modelMapper() {
    	return new ModelMapper();
    }
}
