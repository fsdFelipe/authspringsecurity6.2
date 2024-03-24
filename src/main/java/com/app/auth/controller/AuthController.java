package com.app.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.auth.dto.CredenciaisDto;
import com.app.auth.dto.RegisterDto;
import com.app.auth.security.MyUserDetails;
import com.app.auth.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	AuthenticationManager authenticationMaganer;

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid CredenciaisDto credenciaisDto){
		try {
		var usernamePassword = new UsernamePasswordAuthenticationToken(credenciaisDto.email(), credenciaisDto.senha());
		Authentication auth = authenticationMaganer.authenticate(usernamePassword);
		
		UserDetails usuario = (MyUserDetails) auth.getPrincipal();

		return ResponseEntity.ok(usuario);
		} catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
	}

	 @PostMapping("/register")
	    public ResponseEntity<?> register(@RequestBody @Valid RegisterDto registerDto){
	        return ResponseEntity.ok(authService.salvar(registerDto));
	    }
}
