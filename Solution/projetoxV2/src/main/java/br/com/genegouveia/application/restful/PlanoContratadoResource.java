package br.com.genegouveia.application.restful;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.genegouveia.domain.entity.contrato.PlanoContratado;
import br.com.genegouveia.domain.service.PlanoContratadoService;
import javassist.NotFoundException;

@RestController
@RequestMapping(path = "v1/planos")
public class PlanoContratadoResource {
	@Autowired
	private PlanoContratadoService planoContratadoService;
	
	@GetMapping(path = "/consultar-valores-compilados-planos")
	public ResponseEntity<?> consultarValoresCompiladosDosPlanos() {
		HashMap<String, Double> map = this.planoContratadoService.visualizarValoresCompiladosDosPlanos();
		return ResponseEntity.ok(map);
	}

	
	 @PostMapping(path = "/salvar-plano-contratado")
	 public PlanoContratado salvarPlanoContratado(@RequestBody PlanoContratado planoContratado) throws NotFoundException{
		return this.planoContratadoService.salvarPlanoContratado(planoContratado);
	  }
	 
	 @GetMapping(path = "/consultar-planos-contratados-disponiveis")
	 public List<PlanoContratado> consultarPLanoContratadosDisponiveisDoContratado(@RequestBody String numeroContrato){
		return this.planoContratadoService.consultarPlanosDisponiveisDoContrato(numeroContrato);
		 
	 }
	 @PostMapping(path = "/remover-plano-contratado")
	 public void removerPLanoContratado(@RequestBody long planoContratadoId) {
		 this.removerPLanoContratado(planoContratadoId);
	 }
	 
}

	