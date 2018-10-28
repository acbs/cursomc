package com.andrecarlos.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.andrecarlos.cursomc.security.UserSS;

public class UserService {

	// Obtendo usuário logado
	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
}
