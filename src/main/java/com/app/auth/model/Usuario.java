package com.app.auth.model;

import java.util.HashSet;
import java.util.Set;

import com.app.auth.enums.Perfil;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private String email;
	private String senha;
	
	@ElementCollection
	@CollectionTable(name="Perfil")
	private Set<Perfil> perfis = new HashSet<>();

	public Usuario(String email, String senha) {
		this.email = email;
		this.senha = senha;
		addPerfil(Perfil.USUARIO);
	}
	
	public void addPerfil(Perfil perfil) {
        perfis.add(perfil);
    }
}
