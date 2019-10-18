package com.br.projetox.security;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;

import com.br.projetox.entity.Usuario;

public abstract class RequestContext {
	/*
	 * @return
	 */
	public static Optional<Usuario> currentUser()
	{
		return Optional.ofNullable( SecurityContextHolder.getContext().getAuthentication() )
				.map( auth -> auth.getPrincipal() instanceof Usuario ? (Usuario) auth.getPrincipal() : null );
	}

}
