package com.app.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.auth.dto.UsuarioDto;
import com.app.auth.model.Usuario;
import com.app.auth.services.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping
	public String bemVindo() {
		return "Bem Vindo !";
	}
	
	@PostMapping
	public Usuario usuario(@RequestBody @Valid UsuarioDto usuarioDto) {
		return usuarioService.salvar(usuarioDto);
		}
	
	@PreAuthorize("hasAuthority('SCOPE_USUARIO')")
    @GetMapping("/user")
	public String usuarioEndpoint() {
		return "Bem Vindo !\\n Esta é uma rota q todos usuarios podem ver";
	}
	@PreAuthorize("hasAnyAuthority('SCOPE_MANAGER', 'SCOPE_ADMIN')")
	@GetMapping("/manager")
	public String managerEndpoint() {
		return "Bem Vindo !\\n Esta é uma rota q apenas managers e admins podem ver";
	}

	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@GetMapping("/admin")
	public String adminEndpoint() {
		return "Bem Vindo !\\n Esta é uma rota q apenas admins podem ver";
	}
}
