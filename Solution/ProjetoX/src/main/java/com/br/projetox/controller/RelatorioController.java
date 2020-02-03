package com.br.projetox.controller;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.projetox.entity.Relatorio;
import com.br.projetox.service.RelatorioService;

@RestController
@RequestMapping(path = "v1/relatorios")
public class RelatorioController {
	@Autowired
	private RelatorioService relatorioService;
	
	@GetMapping()
	public PagedListHolder<Relatorio> getRelatorios(@RequestParam("page") int page,@RequestParam("size") int size,@RequestParam("ano") int ano , @RequestParam("mes") int mes) throws IOException {
		PageRequest pageable = PageRequest.of(page, size);
	
		List<Relatorio> relatorios = this.relatorioService.montarEntidadeRelatorio(ano, mes);
		PagedListHolder<Relatorio> pageList = new PagedListHolder<Relatorio>(relatorios);
		pageList.setPage(page);
		pageList.setPageSize(size);
		return pageList; 
		
	}
	
	@GetMapping(path = "/export-relatorio")
	public  ResponseEntity<ByteArrayResource> planilhaRegistrosExport(@RequestParam(name = "ano") int ano, @RequestParam(name = "mes") int mes) throws IOException {
		ByteArrayOutputStream planilhaRegistro =  this.relatorioService.createPlanilhaRelatorio(ano, mes);
		 HttpHeaders header = new HttpHeaders();
		 header.set("Content-type","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
         return new ResponseEntity<>(new ByteArrayResource(planilhaRegistro.toByteArray()),
                 header, HttpStatus.CREATED);
	}
}
