package com.br.projetox.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.projetox.entity.Contrato;
import com.br.projetox.service.ContratoService;

import javassist.NotFoundException;

@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 4800, allowCredentials = "false")
@RestController
@RequestMapping(path = "/contratos")
public class ContratoController {
	@Autowired
	private ContratoService service;
	
	@GetMapping
	public ResponseEntity<Page<Contrato>> findAll(@RequestParam(name = "page" , required = true) Integer page , @RequestParam(name = "size",required = true)
	Integer size){
		Page<Contrato> returnPage = null;
		returnPage = this.service.findAll(page, size);
		return ResponseEntity.ok(returnPage);
}
	@GetMapping(path = "/{numeroContrato}")
	public ResponseEntity<Optional<Contrato>> findBynumeroContrato(@Valid @PathVariable int numeroContrato) throws NotFoundException{
		Optional<Contrato> contrato = null;
		contrato = this.service.findByNumeroContrato(numeroContrato);
		return ResponseEntity.ok(contrato);
		
	}
}