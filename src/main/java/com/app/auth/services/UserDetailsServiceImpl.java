package com.app.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.auth.model.Usuario;
import com.app.auth.repositories.UsuarioRepository;
import com.app.auth.security.MyUserDetails;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(email);

		if (usuario == null) {
			throw new UsernameNotFoundException("Email não existe: "+email);
		}

		return new MyUserDetails(usuario, usuario.getPerfis());
	}
}
