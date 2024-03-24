package com.app.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.app.auth.dto.RSAKeyRecord;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private RSAKeyRecord rsaKeyRecord;
	
	private static final String[] PUBLIC_MATHERS_GET = {
			"/h2-console/**",
			"/usuarios/**",
			"/auth/**"
	};
	private static final String[] PUBLIC_MATHERS_POST = {
			"/h2-console/**",
			"/usuarios/**",
			"/auth/**"
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
	                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())))
	                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                .build();
	    }
	 
	 @Bean
		AuthenticationManager authenticatioManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
			return authenticationConfiguration.getAuthenticationManager();
		}
	 
	 @Bean
	    PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	 
	 @Bean
	    JwtDecoder jwtDecoder(){
	        return NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();
	    }

	 @Bean
	    JwtEncoder jwtEncoder(){
	        JWK jwk = new RSAKey.Builder(rsaKeyRecord.rsaPublicKey()).privateKey(rsaKeyRecord.rsaPrivateKey()).build();
	        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
	        return new NimbusJwtEncoder(jwkSource);
	    } 

}
