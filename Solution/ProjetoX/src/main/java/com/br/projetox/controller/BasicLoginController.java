package com.br.projetox.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicLoginController {
	
	@RequestMapping("/login")
	public Principal login(Principal user) {
		return user;
	}
}
