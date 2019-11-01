package com.br.projetox.test.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.TipoContrato;
import com.br.projetox.repository.ContratoRepository;
import com.br.projetox.repository.ServicoRepository;
import com.br.projetox.service.PlanoContratadoService;

import javassist.NotFoundException;

public class PlanoContratadoServiceTest extends AbstractIntegrationTest {
	@Autowired
	private PlanoContratadoService planoContratoService;
	
	@Autowired
	private ContratoRepository contratoRepository;
	
	@Autowired 
	private ServicoRepository servicoRepository;
	
	@WithUserDetails("marcieli.langer@mailinator.com")
	@Sql({	"/dataset/truncate.sql",
			"/dataset/Usuario.sql",
			"/dataset/Servico.sql",
			"/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql"})
	@Test
	public void findPlanoContratadoAtivoTestMustPass() throws NotFoundException {
		final Long servicoId = this.servicoRepository.findById(1L).orElse(null).getId();
		final Long contratoId = this.contratoRepository.findById(1L).orElse(null).getId();
		
		final PlanoContratado plano = this.planoContratoService.findPlanoContratadoAtivo(servicoId, contratoId, TipoContrato.PLANO);
		
		Assert.assertNotNull(plano);
		Assert.assertNotNull(plano.getId());
	}
	
}
