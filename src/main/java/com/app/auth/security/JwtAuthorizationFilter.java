package com.app.auth.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.app.auth.dto.RSAKeyRecord;
import com.app.auth.enums.TokenType;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
	
	@Autowired
	private RSAKeyRecord rsaKeyRecord;
	@Autowired
	private JwtValidateToken jwtValidateToken;

	public JwtAuthorizationFilter(RSAKeyRecord rsaKeyRecord, JwtValidateToken jwtValidateToken) {
		this.rsaKeyRecord = rsaKeyRecord;
		this.jwtValidateToken = jwtValidateToken;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			log.info("[JwtAccessTokenFilter:doFilterInternal] :: Started ");

            log.info("[JwtAccessTokenFilter:doFilterInternal]Filtering the Http Request:{}",request.getRequestURI());

            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if(authHeader == null || authHeader != null && authHeader.startsWith(TokenType.Bearer.name())){
                filterChain.doFilter(request,response);
                return;
            }

            JwtDecoder jwtDecoder =  NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();

            final String token = authHeader.substring(7);
            final Jwt jwtToken = jwtDecoder.decode(token);

            final String userName = jwtValidateToken.getUserName(jwtToken);

            if(!userName.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null){

                UserDetails userDetails = jwtValidateToken.userDetails(userName);
                if(jwtValidateToken.isTokenValid(jwtToken,userDetails)){
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                    UsernamePasswordAuthenticationToken createdToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    createdToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(createdToken);
                    SecurityContextHolder.setContext(securityContext);
                }
            }
            log.info("[JwtAccessTokenFilter:doFilterInternal] Completed");

            filterChain.doFilter(request,response);
		}catch(JwtValidationException jwtValidationException) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, jwtValidationException.getMessage());
		}
		
	}

}
