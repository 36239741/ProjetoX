package com.br.projetox.test.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;

import com.br.projetox.entity.TipoContrato;
import com.br.projetox.service.ContratoService;
import com.br.projetox.service.PlanoContratadoService;
import com.br.projetox.service.ServicoService;

import javassist.NotFoundException;

public class TestPlanoContratado extends AbstractIntegrationTest {
	@Autowired
	private PlanoContratadoService planoContratoService;
	@Autowired
	private ContratoService contratoService;
	
	@Autowired 
	ServicoService servicoService;
	
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Test
	public void findByPlanoContratoFindByPlanoContrato() throws NotFoundException {
		Assert.assertNotNull(this.planoContratoService.findPlanoContratado(this.servicoService.findServico("Fonoaudiologia").getId(),this.contratoService.findByNumeroContrato("2").getId(),
				TipoContrato.PLANO.ordinal()));
	}
	
}
