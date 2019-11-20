package com.br.projetox.controller;

import java.util.HashMap;

import javax.annotation.processing.FilerException;
import javax.validation.Valid;

import org.directwebremoting.io.FileTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.br.projetox.entity.Contrato;
import com.br.projetox.service.ContratoService;

import javassist.NotFoundException;

@Component
@RestController
@RequestMapping(path = "/contratos")
public class ContratoController {
	@Autowired
	private ContratoService service;
	
	
	

	@GetMapping
	public ResponseEntity<Page<Contrato>> findAll(@RequestParam(name = "page" , required = true) Integer page ,
			@RequestParam(name = "size",required = true)Integer size, @RequestParam(name = "sort") String sort,
			@RequestParam(name = "atributo") String atributo){
		Page<Contrato> returnPage = null;
		Direction direction =null;
		direction = sort.trim().equals("ASC")? Direction.ASC : Direction.DESC;
		returnPage = this.service.findAll(page, size,direction,atributo);
		return ResponseEntity.ok(returnPage);
	}
	
	@GetMapping(path = "/{numeroContrato}")
	public ResponseEntity<Contrato> findBynumeroContrato(@Valid @PathVariable String numeroContrato) throws NotFoundException{
		Contrato contrato = null;
		contrato = this.service.findByContractNumber(numeroContrato);
		return ResponseEntity.ok(contrato);
		
	}
	@GetMapping(path = "/filter")
	public ResponseEntity<Page<Contrato>> findByFilters(@RequestParam(name = "numero") String numero,
			@RequestParam(name = "nomePaciente") String nomePaciente, @RequestParam(name = "page") int page,
			@RequestParam(name = "size") int size, @RequestParam(name = "ativo") String ativo){
		Page<Contrato> pageable = null;
		if(ativo.equals("true") || ativo.equals("false")) {
			pageable = this.service.findByFiltersParamActive(numero,
					nomePaciente, 
					PageRequest.of(page, size), 
					Boolean.parseBoolean(ativo));	
		}
		else {
			pageable = this.service.findByFilters(numero, nomePaciente, PageRequest.of(page, size));
		}
		return ResponseEntity.ok(pageable);
	}
	@PostMapping
	public ResponseEntity<?> importContratos(@RequestParam("file") MultipartFile file) throws Exception  {
		HashMap<String, Integer> map = null;
		ResponseEntity<?> responseEntityOK = null;
		if(file.isEmpty() == false) {
			if(file.getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || 
					file.getContentType().equalsIgnoreCase("application/vnd.ms-excel")) {
				FileTransfer fileTransfer = new FileTransfer(file.getName(), file.getContentType(), file.getInputStream());
				map = this.service.importPlanilhaContratos(fileTransfer);
				responseEntityOK = ResponseEntity.ok(map);
			}
			else {
				throw new FilerException("Tipo de arquivo nao suportado");
			}
		}
		else {
			throw new FilerException("Arquivo de Upload Vazio");
		}
		
		return responseEntityOK;
	}
}