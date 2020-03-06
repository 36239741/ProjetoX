package com.br.projetox.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.Servico;
import com.br.projetox.service.PlanoContratadoService;

import javassist.NotFoundException;

@RestController
@RequestMapping(path = "v1/planos")
public class PlanoContratadoController {
	@Autowired
	private PlanoContratadoService planoContratadoService;
	
	@GetMapping(path = "/consultar-soma-total-planos")
	public ResponseEntity<?> findTotalActiveContracts() {
		HashMap<String, Double> map = this.planoContratadoService.consultarSomaTotalPlanosAtivos();
		return ResponseEntity.ok(map);
	}

	
	 @GetMapping(path = "/find-plano-contratado")
	 public void findPlanoContratado(@RequestBody Map<String, Object> planoContratado) throws NotFoundException{
		 this.planoContratadoService.mapearPlanoContratado(planoContratado);
	  }
	 
	 @GetMapping(path = "/find-all")
	 public ResponseEntity<?> findAllPlanoContratadoByContratoId(@RequestParam("numero-contrato") String numeroContrato){
		 List<PlanoContratado> list = this.planoContratadoService.consultarPlanoContratadoAtivoPorContrato(numeroContrato);
		 return ResponseEntity.ok(list);
	 }
	 
	@PostMapping
	public void salvarPlanoContratado(@RequestBody Map<String, Object> planoContratado) throws NotFoundException{
		this.planoContratadoService.salvarPlanoContratado(planoContratado);
	}
	
	@PutMapping
	public void atualizarPlanoContratado(@RequestBody Map<String, Object> planoContratado) throws NotFoundException {
		this.planoContratadoService.atualizarPlanoContratado(planoContratado);
	}
	
	@PutMapping(path = "/deletar")
	public void deleteLogical(@RequestBody String planoContratadoId) {
		this.planoContratadoService.deleteLogico(planoContratadoId);
	}
}

	