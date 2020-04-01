package com.br.projetox.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.projetox.entity.Contrato;
import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.Servico;
import com.br.projetox.service.PlanoContratadoService;

import javassist.NotFoundException;

@RestController
@RequestMapping(path = "v1/planos")
public class PlanoContratadoController {
	@Autowired
	private PlanoContratadoService planoContratadoService;

	@GetMapping(path = "/consultar-valores-compilados-planos")
	public ResponseEntity<?> consultarValoresCompiladosDosPlanos() {
		HashMap<String, Double> map = this.planoContratadoService.visualizarValoresCompiladosDosPlanos();
		return ResponseEntity.ok(map);
	}

	@PostMapping(path = "/salvar-plano-contratado")
	public PlanoContratado salvarPlanoContratado(@RequestBody PlanoContratado planoContratado)
			throws NotFoundException {
		return this.planoContratadoService.salvarPlanoContratado(planoContratado);
	}

	@GetMapping(path = "/consultar-planos-contratados-disponiveis")
	public List<PlanoContratado> consultarPLanoContratadosDisponiveisDoContratado(@RequestBody String numeroContrato) {
		return this.planoContratadoService.consultarPlanosDisponiveisDoContrato(numeroContrato);

	}

	@PostMapping(path = "/remover-plano-contratado")
	public void removerPLanoContratado(@RequestBody long planoContratadoId) {
		this.planoContratadoService.removerPlanoContratado(planoContratadoId);
	}

	@GetMapping(path = "/listar-planos-por-contrato")
	public List<PlanoContratado> listarPlanosAtivosPorContrato(@RequestParam(name = "numeroContrato") String numeroContrato){
			List<PlanoContratado> planos = this.planoContratadoService.listarPlanosAtivosPorContrato(numeroContrato);
			
			planos.forEach((PlanoContratado plano ) -> {
				this.clearPlano(plano);
			});
			
		 	return planos;
	}

	private void clearContrato(Contrato contrato) {
		Assert.notNull(contrato, "Contrato não definido.");
		contrato.setCreated(null);
		contrato.setDesconto(null);
		contrato.setDiferenca(null);
		contrato.setPlanoContratado(null);
		contrato.setRegistro(null);
		contrato.setTipoContratoTransient(null);
		contrato.setUpdated(null);
		contrato.setValorExecutado(null);
		contrato.setUsuario(null);
		contrato.setValorTotal(null);
		contrato.setAtivo(null);
	}
	
	private void clearServico(Servico servico) {
		Assert.notNull(servico, "Serviço não definido.");
		servico.setCreated(null);
		servico.setUpdated(null);
		servico.setUsuario(null);
		servico.setValor(null);
	}

	private void clearPlano(PlanoContratado plano) {
		Assert.notNull(plano, "Plano não definido.");
		plano.setAtivo(null);
		this.clearContrato(plano.getContrato());
		plano.setCreated(null);
		plano.setSaldoMensal(null);
		this.clearServico(plano.getServico());
		plano.setTipoContrato(null);
		plano.setUpdated(null);
		plano.setUsuario(null);
		plano.setValorAtendimento(null);
		plano.setValorSessao(null);
		plano.setValorTotal(null);
	}
}
