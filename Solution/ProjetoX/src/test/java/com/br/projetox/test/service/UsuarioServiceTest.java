package com.br.projetox.test.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import com.br.projetox.entity.Usuario;
import com.br.projetox.repository.UsuarioRepository;

public class UsuarioServiceTest extends AbstractIntegrationTest {
	@Autowired
	private  UsuarioRepository repo;
	
	/*
	 * ==================== TESTES DE SALVAR USUÁRIO ==============================
	 */
	@Test
	public void saveUsuarioMustPass() {
		Usuario user = new Usuario();
		user.setEmail("henrique_nitatori@hotmail.com");
		user.setSenha(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("123456"));
		this.repo.save(user);
	}
}
