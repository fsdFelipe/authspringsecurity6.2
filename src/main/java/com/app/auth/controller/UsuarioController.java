package com.app.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
}
