package com.obviousai.inventoryms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	void configGlobal(AuthenticationManagerBuilder builder) throws Exception {
		builder.inMemoryAuthentication().
			withUser("user").password(passwordEncoder.encode("password"));
//			.authorities(Arrays.asList());
	}
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests(auth -> {
				try {
					auth.anyRequest().authenticated()
						.and().httpBasic();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
        return http.build();
    }
}
