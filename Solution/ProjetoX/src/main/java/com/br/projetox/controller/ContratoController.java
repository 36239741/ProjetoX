package com.br.projetox.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.processing.FilerException;
import javax.validation.Valid;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.directwebremoting.io.FileTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.br.projetox.entity.Contrato;
import com.br.projetox.service.ContratoService;

import javassist.NotFoundException;

@Component
@RestController
@RequestMapping(path = "v1/contratos")
public class ContratoController {
	@Autowired
	private ContratoService service;
	
	
	@GetMapping(path = "/find-by-biometria")
	public ResponseEntity<Contrato> findByBiometria() throws UnsupportedEncodingException{
		Contrato contrato = this.service.findByBiometria();
		return ResponseEntity.ok(contrato);
	}
	
	@GetMapping(path = "/cancel-capture")
	public void cancelCapture() {
		this.service.cancelCaptureFingerPrint();
		
	}
	
	@PostMapping(path = "/save-biometria")
	public void saveFingerprint(@RequestBody String numeroContrato) throws NotFoundException {
		this.service.saveFingerprint(numeroContrato);
	}
	
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
	
	@PostMapping(path = "/desconto")
	public Contrato gerarDesconto(@RequestBody String valorDesconto, @RequestParam(name = "numeroContrato") String numeroContrato) throws NumberFormatException, Exception {
		return this.service.calcularDesconto(numeroContrato, Double.parseDouble(valorDesconto));
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
			@RequestParam(name = "size") int size, @RequestParam(name = "ativo") String ativo, @RequestParam(name = "sort") String sort,
			@RequestParam(name = "atributo") String atributo){
		Page<Contrato> pageable = null;
		Direction direction =null;
		direction = sort.trim().equals("ASC")? Direction.ASC : Direction.DESC;
		if(ativo.equals("true") || ativo.equals("false")) {
			pageable = this.service.findByFiltersParamActive(numero,
					nomePaciente, 
					PageRequest.of(page, size,direction, atributo), 
					Boolean.parseBoolean(ativo));	
		}
		else {
			pageable = this.service.findByFilters(numero, nomePaciente, PageRequest.of(page, size, org.springframework.data.domain.Sort.by(direction, atributo)));
		}
		return ResponseEntity.ok(pageable);
	}
	@PostMapping
	public ResponseEntity<?> importContratos(@RequestParam("file") MultipartFile file) throws Exception  {
		HashMap<String, Integer> map = null;
		ResponseEntity<?> responseEntityOK = null;
		if(file.isEmpty() == false) {
			/*if(file.getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || 
					file.getContentType().equalsIgnoreCase("application/vnd.ms-excel")) {*/
				FileTransfer fileTransfer = new FileTransfer(file.getName(), file.getContentType(), file.getInputStream());
				map = this.service.importPlanilhaContratos(fileTransfer);
				responseEntityOK = ResponseEntity.ok(map);
				/*}
			else {
				throw new FilerException("Tipo de arquivo não suportado.");
			}*/
		}
		else {
			throw new FilerException("Arquivo de Upload vazio.");
		}
		
		return responseEntityOK;
	}
	
	@GetMapping(path = "/relatorios")
	public PagedListHolder<Contrato> getRelatorio(@RequestParam("ano") int ano, @RequestParam("mes") int mes,@RequestParam("page") int page,@RequestParam("size") int size) {
		List<Contrato> contratos = this.service.montarEntidade(ano, mes);
		PagedListHolder<Contrato>  pageList = new PagedListHolder<>(contratos);
		pageList.setPage(page);
		pageList.setPageSize(size);
		return pageList;
	}
	
	@GetMapping(path = "/export-relatorio")
	public  ResponseEntity<ByteArrayResource> planilhaRegistrosExport(@RequestParam("ano") int ano, @RequestParam("mes") int mes) throws IOException {
		ByteArrayOutputStream planilhaRegistro =  this.service.createPlanilhaRelatorio(ano, mes);
		 HttpHeaders header = new HttpHeaders();
		 header.set("Content-type","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
         return new ResponseEntity<>(new ByteArrayResource(planilhaRegistro.toByteArray()),
                 header, HttpStatus.CREATED);
	}
	
}