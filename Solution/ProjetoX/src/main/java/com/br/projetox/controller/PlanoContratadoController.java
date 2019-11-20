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
import com.br.projetox.service.PlanoContratadoService;

import javassist.NotFoundException;

@RestController
@RequestMapping(path = "/planos")
public class PlanoContratadoController {
	@Autowired
	private PlanoContratadoService planoContratadoService;
	
	@GetMapping(path = "/valor-total")
	public ResponseEntity<?> findTotalActiveContracts() {
		HashMap<String, Double> map = this.planoContratadoService.findTotalActiveContracts();
		return ResponseEntity.ok(map);
	}

	
	 @GetMapping(path = "/find-plano-contratado")
	 public void findPlanoContratado(@RequestBody Map<String, Object> planoContratado){
		 System.out.println(planoContratado.toString());
	  }
	 
	 @GetMapping(path = "/find-all")
	 public ResponseEntity<?> findAllPlanoContratadoByContratoId(@RequestParam("id") String contratoId){
		 List<PlanoContratado> list = this.planoContratadoService.findAllPlanoContratadoByContratoId(contratoId);
		 return ResponseEntity.ok(list);
	 }

	@PostMapping
	public void savePlanoContrato(@RequestBody Map<String, Object> planoContratado) throws NotFoundException{
		this.planoContratadoService.savePlanoContratato(planoContratado);
	}
	
	@PutMapping
	public void updatePlanoContrato(@RequestBody Map<String, Object> planoContratado) throws NotFoundException {
		this.planoContratadoService.updatePlanoContrato(planoContratado);
	}
	@PostMapping(path = "/delete")
	public void deleteLogical(@RequestBody Map<String, Object> delete) {
		this.planoContratadoService.deleteLogical(delete);
	}
}
	