package com.br.projetox.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.projetox.entity.Registro;
import com.br.projetox.exception.RegistroException;
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
	public void saveHorarioSaida(@RequestBody String numeroContrato) throws NotFoundException {
		this.registroService.saveHorarioSaida(numeroContrato);
	}
	
	@GetMapping(path = "/find-all")
	public Page<Registro> findAllRegistro(@RequestParam(name = "page" , required = true) Integer page ,
			@RequestParam(name = "size",required = true)Integer size, @RequestParam(name= "numeroContrato") String numeroContrato){
		return this.registroService.findAllRegistro(numeroContrato, page, size);
	}
	
	@PostMapping(path = "/trocar-servico")
	public Registro findAllRegistro(@RequestParam(name = "situacaoRegistro") String situacaoRegistro,@RequestParam(name = "registroId") String registroId,
			@RequestParam(name = "servico") String servico,@RequestParam(name = "valorSessao") String valorSessao){
		if(servico != null && valorSessao != null) {
			return this.registroService.exchangeOfContractStatus(situacaoRegistro, Long.parseLong(registroId), servico, Double.parseDouble(valorSessao));
		}
		else {
			throw new RegistroException("Existem valores nulos");
		}
	}
	@GetMapping(path = "/find-by-date")
	public Page<Registro> findByDate(@RequestParam(name = "dataInicial") String dataInicial, @RequestParam(name = "dataFinal") String dataFinal,
			@RequestParam(name = "contratoId") String contratoId, @RequestParam(name = "page") int page, @RequestParam(name = "size") int size){
		return this.registroService.findByDate(dataInicial, dataFinal, contratoId, page, size);
	}
	
}
