package com.app.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.auth.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

	Usuario findByEmail(String email);

}
