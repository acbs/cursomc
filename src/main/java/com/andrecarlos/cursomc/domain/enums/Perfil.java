package com.andrecarlos.cursomc.domain.enums;

public enum Perfil {

	// ROLE_ prefixo exigido pelo framework
	ADMIN(1, "ROLE_ADMIN"),
	CLIENTE(2, "ROLE_CLIENTE");
	
	private int codigo;
	private String descricao;
	
	private Perfil(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static Perfil toEnum(Integer codigo) {
		if (codigo == null) {
			return null;
		}
		
		for (Perfil cod : Perfil.values()) {
			if (codigo.equals(cod.getCodigo())) {
				return cod;
			}
		}
		throw new IllegalArgumentException("Id inválido: " + codigo);
	}
}
