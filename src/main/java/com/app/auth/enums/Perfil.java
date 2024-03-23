package com.app.auth.enums;

public enum Perfil {
	 ADMIN(1,"ADMIN"),
	 MANAGER(2,"MANAGER"),
	 USUARIO(3,"USUARIO");
		
		private int id;
	    private String descricao;

	    Perfil(int id, String descricao) {
	    	this.id = id;
	        this.descricao = descricao;
	    }

	    public String getDescricao() {
	        return descricao;
	    }
	    
	    public int getId() {
	        return id;
	    }

	    public static Perfil fromString(String descricao) {
	        for (Perfil perfil : Perfil.values()) {
	            if (perfil.getDescricao().equalsIgnoreCase(descricao)) {
	                return perfil;
	            }
	        }
	        throw new IllegalArgumentException("Perfil inválido: " + descricao);
	    }
}
