package br.com.genegouveia.application.restful;

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

import br.com.genegouveia.domain.entity.contrato.Contrato;
import br.com.genegouveia.domain.service.contrato.ContratoService;
import javassist.NotFoundException;

@Component
@RestController
@RequestMapping(path = "v1/contratos")
public class ContratoResource {
	@Autowired
	private ContratoService service;
	
	

	
	@GetMapping
	public ResponseEntity<Page<Contrato>> consultarContratos(@RequestParam(name = "page" , required = true) Integer page ,
			@RequestParam(name = "size",required = true)Integer size, @RequestParam(name = "sort") String sort,
			@RequestParam(name = "atributo") String atributo,@RequestParam(name = "nomePaciente") String nomePaciente,
			@RequestParam(name = "statusContrato") Boolean statusContrato,@RequestParam(name = "numeroContrato") String numeroContrato){
		
		Direction direction =null;
		direction = sort.trim().equals("ASC")? Direction.ASC : Direction.DESC;
		PageRequest pageable = PageRequest.of(page, size,
				org.springframework.data.domain.Sort.by(direction, atributo));
		
		Page<Contrato> contratos = null;
		contratos = this.service.consultarTodosContratos(numeroContrato,nomePaciente,statusContrato,pageable);
		return ResponseEntity.ok(contratos);
	}
	
	@PostMapping(path = "/atribuir-desconto")
	public Contrato atribuirDesconto(@RequestParam(name = "valorDesconto") String valorDesconto, @RequestParam(name = "numeroContrato") String numeroContrato) throws Exception {
		return this.service.atribuirDesconto(numeroContrato, Double.parseDouble(valorDesconto));
	}
	
	@GetMapping(path = "/{numeroContrato}")
	public ResponseEntity<Contrato> consultarContrato(@Valid @PathVariable String numeroContrato) throws NotFoundException{
		Contrato contrato = null;
		contrato = this.service.consultarContratoPorNumeroContrato(numeroContrato);
		return ResponseEntity.ok(contrato);
		
	}

	@PostMapping
	public ResponseEntity<?> importarPlanilhaDeContratos(@RequestParam("file") MultipartFile file) throws Exception  {
		HashMap<String, Integer> map = null;
		ResponseEntity<?> responseEntityOK = null;
		if(file.isEmpty() == false) {
			/*if(file.getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || 
					file.getContentType().equalsIgnoreCase("application/vnd.ms-excel")) {*/
				FileTransfer fileTransfer = new FileTransfer(file.getName(), file.getContentType(), file.getInputStream());
				map = this.service.importarPlanilhaContratos(fileTransfer);
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
	
	@GetMapping(path = "/relatorio-mensal")
	public PagedListHolder<Contrato> consultarRelatorioMensal(@RequestParam("ano") int ano, @RequestParam("mes") int mes,@RequestParam("page") int page,@RequestParam("size") int size) {
		List<Contrato> contratos = this.service.consultarRelatorioMensal(ano, mes);
		PagedListHolder<Contrato>  pageList = new PagedListHolder<>(contratos);
		pageList.setPage(page);
		pageList.setPageSize(size);
		return pageList;
	}
	
	@GetMapping(path = "/exportar-planilha-relatorio")
	public  ResponseEntity<ByteArrayResource> exportarPlanilhaRelatorio(@RequestParam("ano") int ano, @RequestParam("mes") int mes) throws IOException {
		ByteArrayOutputStream planilhaRegistro =  this.service.exportarPlanilhaRelatorio(ano, mes);
		 HttpHeaders header = new HttpHeaders();
		 header.set("Content-type","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
         return new ResponseEntity<>(new ByteArrayResource(planilhaRegistro.toByteArray()),
                 header, HttpStatus.CREATED);
	}
	
}