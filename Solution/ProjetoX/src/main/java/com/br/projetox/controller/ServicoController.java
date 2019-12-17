package com.br.projetox.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.projetox.entity.Servico;
import com.br.projetox.service.ServicoService;

@RestController
@RequestMapping(path = "v1/servicos")
public class ServicoController {
	@Autowired
	private ServicoService servicoService;
	
	@GetMapping
	public ResponseEntity<List<Servico>> findAll(){
		List<Servico> servicoList = this.servicoService.findAll();
		return ResponseEntity.ok(servicoList);
	}
}
