package com.app.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private static final String[] PUBLIC_MATHERS_GET = {
			"/h2-console/**",
			"/usuarios/**",
	};
	private static final String[] PUBLIC_MATHERS_POST = {
			"/h2-console/**",
			"/usuarios/**",
	};
	
	 @Bean
	    SecurityFilterChain apiSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
	        return httpSecurity
	                .csrf(AbstractHttpConfigurer::disable)
	                .headers(headers -> headers.disable())
	                .authorizeHttpRequests(auth -> auth
	                		.requestMatchers(HttpMethod.GET, PUBLIC_MATHERS_GET).permitAll()
	                		.requestMatchers(HttpMethod.POST, PUBLIC_MATHERS_POST).permitAll()
	                		.anyRequest().authenticated())
	                .build();
	    }

}