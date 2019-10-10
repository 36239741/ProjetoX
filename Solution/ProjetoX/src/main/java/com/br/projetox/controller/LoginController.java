package com.br.projetox.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.projetox.entity.Usuario;
import com.br.projetox.jwt.JwtGeneration;
import com.br.projetox.jwt.TokenJwt;
@CrossOrigin(origins = {"http://localhost:4200/login"}, maxAge = 4800, allowCredentials = "false")
@RestController
@RequestMapping(path = "/authentication")
public class LoginController {
	
	 @Autowired
	 private AuthenticationManager auth;
	 
	 @Autowired
	 private JwtGeneration jwt;
	
	@PostMapping
	public ResponseEntity<?> loginAuthentication(@RequestBody Usuario usuario) {		
		UsernamePasswordAuthenticationToken dadosLogin = usuario.converter();
		try {
		Authentication authentication = this.auth.authenticate(dadosLogin);
		String token = jwt.gerarToken(authentication);
		return ResponseEntity.ok(new TokenJwt(token, "Bearer "));
		}catch(AuthenticationException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}


















