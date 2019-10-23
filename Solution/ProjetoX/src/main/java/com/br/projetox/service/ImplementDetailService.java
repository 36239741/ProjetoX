package com.br.projetox.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.br.projetox.entity.Usuario;
import com.br.projetox.repository.UsuarioRepository;
@Service
@Transactional
public class ImplementDetailService implements UserDetailsService {

	@Autowired
	private UsuarioRepository repository;
	
	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String email)  {
		Usuario usuario = this.repository.findByEmailIgnoreCase(email);
		Assert.notNull(usuario,"Email ou senha incorretos!!");
		return usuario;
	}	

}
