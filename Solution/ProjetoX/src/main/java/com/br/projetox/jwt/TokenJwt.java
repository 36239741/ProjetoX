package com.br.projetox.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenJwt {
	private String token;
	private String tipo;
	
}
