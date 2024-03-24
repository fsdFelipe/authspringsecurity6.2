package com.app.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.auth.dto.RegisterDto;
import com.app.auth.model.Usuario;
import com.app.auth.repositories.UsuarioRepository;

@Service
public class AuthService {
	@Autowired
	private UsuarioRepository usuarioRepository;

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

}
