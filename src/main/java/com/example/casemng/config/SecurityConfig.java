package com.example.casemng.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		return http.formLogin(login -> login
				.loginPage("/login").permitAll()
				.loginProcessingUrl("/login").permitAll()
				.failureUrl("/login?error")
				.usernameParameter("userId")
				.passwordParameter("password")
				.failureUrl("/login")
				.defaultSuccessUrl("/case"))
				
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login"))
				
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/login", "/css/**").permitAll()
						.requestMatchers("/admin").hasAuthority("ROLE_ADMIN")
						.anyRequest().authenticated())
				
				.csrf(csrf -> csrf.disable())
				
				.build();
	}

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
