package com.obviousai.inventoryms;

import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSecurityConfig {
	/*@Autowired
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
    }*/
}
