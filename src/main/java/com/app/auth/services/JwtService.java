package com.app.auth.services;

import java.time.Duration;
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
	        return 59L;
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
	  
	  public String getTokenExpiryInReadableFormat() {
	        long expirySeconds = getTokenExpiry(); // Obtenha o tempo de expiração real conforme necessário

	        // Converte os segundos em uma duração
	        Duration duration = Duration.ofSeconds(expirySeconds);

	        // Extrai os componentes de tempo da duração
	        long semanas = duration.toDays() / 7;
	        long dias = duration.toDays() % 7;
	        long horas = duration.toHours() % 24;
	        long minutos = duration.toMinutes() % 60;
	        long segundos = duration.getSeconds() % 60;

	        // Constrói a representação de tempo legível
	        StringBuilder sb = new StringBuilder();
	        if (semanas > 0) {
	            sb.append(semanas).append(" semana").append(semanas > 1 ? "s, " : ", ");
	        }
	        if (dias > 0) {
	            sb.append(dias).append(" dia").append(dias > 1 ? "s, " : ", ");
	        }
	        if (horas > 0) {
	            sb.append(horas).append(" hora").append(horas > 1 ? "s, " : ", ");
	        }
	        if (minutos > 0) {
	            sb.append(minutos).append(" minuto").append(minutos > 1 ? "s, " : ", ");
	        }
	            sb.append(segundos).append(" segundos");

	        return sb.toString().replaceAll(", $", "");
	    }

}
