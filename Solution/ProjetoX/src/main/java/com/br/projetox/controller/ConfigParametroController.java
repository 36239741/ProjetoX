package com.br.projetox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.projetox.entity.ConfiguracaoParametro;
import com.br.projetox.service.ConfigParametroService;
@Component
@RestController()
@RequestMapping(path = "v1/config-parametros")
public class ConfigParametroController {
	@Autowired
	private ConfigParametroService configService;
	
	@GetMapping(path = "/{configId}")
	public ResponseEntity<?> findConfigParametros(@PathVariable String configId){
		ConfiguracaoParametro configParametros = this.configService.findConfigParametros(Long.parseLong(configId));
		return ResponseEntity.ok(configParametros);
	}
}
	