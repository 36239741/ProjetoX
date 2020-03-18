package br.com.genegouveia.application.restful;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.genegouveia.domain.entity.contrato.Servico;
import br.com.genegouveia.domain.service.ServicoService;

@RestController
@RequestMapping(path = "v1/servicos")
public class ServicoResource {
	@Autowired
	private ServicoService servicoService;
	
	@GetMapping
	public ResponseEntity<List<Servico>> consultarTodosServicos(){
		List<Servico> servicoList = this.servicoService.consultarTodosServicos();
		return ResponseEntity.ok(servicoList);
	}
}
