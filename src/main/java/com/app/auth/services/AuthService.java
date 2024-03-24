package com.app.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.app.auth.dto.LoginResponseDto;
import com.app.auth.dto.RegisterDto;
import com.app.auth.enums.TokenType;
import com.app.auth.model.Usuario;
import com.app.auth.repositories.UsuarioRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService {
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private JwtService jwtService;

	public Usuario salvar(RegisterDto registerDto) {

		Usuario usuarioExiste = usuarioRepository.findByEmail(registerDto.email());

		if(usuarioExiste != null) {
			throw new RuntimeException( "Email ja cadastrado");
		}
		
		String encryptedPassword = new BCryptPasswordEncoder().encode(registerDto.senha());

		Usuario usuario = new Usuario(registerDto.email(), encryptedPassword);
		if (registerDto.perfil() != null) {
            usuario.addPerfil(registerDto.perfil());
        }
		
		return usuarioRepository.save(usuario);
	}
	
	public LoginResponseDto getJwtTokenAfterAuthentication(Authentication authentication) {
		try {
			Usuario usuario = usuarioRepository.findByEmail(authentication.getName());

			if(usuario == null) {
				log.error("[AuthService:userSignInAuth] User :{} not found",authentication.getName());
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Usuario não existe");
			}

			String accessToken = jwtService.generateToken(authentication);
			long expiry = jwtService.getTokenExpiry();

			log.info("[AuthService:userSignInAuth] Access token for user:{}, has been generated",usuario.getEmail());
	        return  LoginResponseDto.builder()
	                .accessToken(accessToken)
	                .accessTokenExpiry(expiry)
	                .email(usuario.getEmail())
	                .perfis(usuario.getPerfis())
	                .tokenType(TokenType.Bearer)
	                .build();
		}catch(Exception e) {
			log.error("[AuthService:userSignInAuth]Exception while authenticating the user due to :"+e.getMessage());
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please Try Again");
		}
	}

}
