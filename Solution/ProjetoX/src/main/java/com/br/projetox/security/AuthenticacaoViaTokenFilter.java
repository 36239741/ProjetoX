package com.br.projetox.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.br.projetox.entity.Usuario;
import com.br.projetox.jwt.JwtGeneration;
import com.br.projetox.repository.UsuarioRepository;

public class AuthenticacaoViaTokenFilter extends OncePerRequestFilter {
	private JwtGeneration jwtGeneration;
	private UsuarioRepository repository;
	
	public AuthenticacaoViaTokenFilter(JwtGeneration jwtGeneration, UsuarioRepository repository) {
		this.jwtGeneration = jwtGeneration;
		this.repository = repository;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = recuperarToken(request);
		boolean valido = this.jwtGeneration.isTokenValido(token);
		if(valido) {
			autenticarCliente(token);
		}
		filterChain.doFilter(request, response);
	}

	private void autenticarCliente(String token) {
		long idUsuario = this.jwtGeneration.getIDUsuario(token);
		Usuario usuario = this.repository.findById(idUsuario).get();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario,null ,usuario.getAuthorities());;
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
	}

	private String recuperarToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(token == null || token.isEmpty() || token.startsWith("	 ")) {
			return null;
		}
		return token.substring(7, token.length());
	}
	
}















