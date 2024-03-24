package com.app.auth.services;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {
	private final JwtEncoder encoder;

	  public JwtService(JwtEncoder encoder) {
	    this.encoder = encoder;
	  }
	  
	  public long getTokenExpiry() {
	        return 36000L;
	    }

	  public String generateToken(Authentication authentication) {
		 log.info("[JwtTokenGenerator:generateAccessToken] Token Creation Started for:{}", authentication.getName());
	    Instant now = Instant.now();

	    String scope = authentication
	        .getAuthorities().stream()
	        .map(GrantedAuthority::getAuthority)
	        .collect(Collectors
	            .joining(" "));

	    JwtClaimsSet claims = JwtClaimsSet.builder()
	        .issuer("authspringsecurity6.2")
	        .issuedAt(now)
	        .expiresAt(now.plusSeconds(getTokenExpiry()))
	        .subject(authentication.getName())
	        .claim("scope", scope)
	        .build();

	    return encoder.encode(
	        JwtEncoderParameters.from(claims))
	        .getTokenValue();
	  }

}
