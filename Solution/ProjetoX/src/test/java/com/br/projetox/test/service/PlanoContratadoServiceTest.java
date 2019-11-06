package com.br.projetox.test.service;

import java.util.List;

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
	
										/*VERIFICA A BUSCA DE UM PLANO CONTRATADO*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
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
	

	
										/* VERIFICA O RETORNO DE LISTA DE PLANO CONTRATADO */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({	"/dataset/truncate.sql",
			"/dataset/Usuario.sql",
			"/dataset/Servico.sql",
			"/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql"})
	@Test
	public void findbyContractIdTestMustPassVerificandoABuscaPorContrato()  {
		final Long contratoId = 1L;
		List<PlanoContratado> planoContratado = this.planoContratoService.findByContractId(contratoId);
		
		Assert.assertNotNull(planoContratado);
		Assert.assertEquals(1, planoContratado.size());
	}
	
										/* TESTE DE BUSCA DE TODOS OS PLANOS CONTRTADOS */
	@Test
	public void findAllMustPass() {
		final List<PlanoContratado> planoContratado = this.planoContratoService.findAll();
		Assert.assertNotNull(planoContratado);
		Assert.assertEquals(0, planoContratado.size());;
		
	}
	
}
