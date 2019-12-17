package com.br.projetox.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.projetox.entity.Contrato;
import com.br.projetox.service.RegistroService;

import javassist.NotFoundException;

@RestController
@RequestMapping(path = "v1/registros")
public class ResgistroController {
	@Autowired
	private RegistroService registroService;
	
	@PostMapping(path = "/save-entrada")
	public void saveHorarioEntrada(@RequestBody Map<String, Object> map) throws NumberFormatException, NotFoundException {
		 this.registroService.saveHorarioEntrada(map.get("numeroContrato").toString(), map.get("idPlanoContratado").toString());
		 
	}
	
	@PostMapping(path = "/save-saida")
	public void saHorarioSaida(@RequestBody String numeroContrato) throws NotFoundException {
		this.registroService.saveHorarioSaida(numeroContrato);
	}
}
