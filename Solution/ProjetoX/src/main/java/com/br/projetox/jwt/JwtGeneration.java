package com.br.projetox.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.br.projetox.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtGeneration {

	@Value("${forum.jwt.expiration}")
	private String expiration;
	
	@Value("${forum.jwt.secretKey}")
	private String keySecret;
	
	public String gerarToken(Authentication authentication) {
		Usuario logado = (Usuario) authentication.getPrincipal();
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		
		return Jwts.builder()
				.setIssuer("ProjetoX System")
				.setSubject(String.valueOf(logado.getId()))
				.setIssuedAt(hoje)
				.setExpiration(dataExpiracao)
				.signWith(SignatureAlgorithm.HS256, keySecret)
				.compact();
	}

	public boolean isTokenValido(String token) {
		try {
		Jwts.parser().setSigningKey(this.keySecret).parseClaimsJws(token);
		return true;
		}catch(Exception e){
			return false;
		}
	}

	public long getIDUsuario(String token) {
		Claims body = Jwts.parser().setSigningKey(this.keySecret).parseClaimsJws(token).getBody();
		return Long.parseLong(body.getSubject());
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
