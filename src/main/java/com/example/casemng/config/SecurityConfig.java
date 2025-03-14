package com.example.casemng.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		return http
				.formLogin(login -> login
						.loginPage("/login").permitAll()
						.loginProcessingUrl("/login").permitAll()
						.failureUrl("/login?error")
						.usernameParameter("userId")
						.passwordParameter("password")
						.failureUrl("/login")
						.defaultSuccessUrl("/case", true))

				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login"))

				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/login", "/css/**").permitAll()
						.requestMatchers(PathRequest.toH2Console()).permitAll()
						.requestMatchers("/admin").hasAuthority("ROLE_ADMIN")
						.anyRequest().authenticated())

				.headers(headers -> headers
						.frameOptions(FrameOptionsConfig::disable))

				.csrf(csrf -> csrf
						.ignoringRequestMatchers(PathRequest.toH2Console())

				//.disable()
				)
				.build();
	}

	@Bean
	BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
