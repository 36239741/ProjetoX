package com.br.projetox.test.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.br.projetox.entity.Contrato;
import com.br.projetox.service.ContratoService;

import javassist.NotFoundException;

public class TestContratoService extends AbstractIntegrationTest {
	@Autowired
	private ContratoService service;
	
	@Test
	public void testFindAllMustPass() {
		Page<Contrato> page = null;
		page = this.service.findAll(0,1);
		Assert.assertNotNull(page);
	}
	@Test
	public void testFindByNumeroContratoMustPass() throws NotFoundException {
		Contrato contrato = null;
		String numeroContrato = "1";
		contrato = this.service.findByNumeroContrato(numeroContrato);
		Assert.assertNotNull(contrato);
		Assert.assertEquals(numeroContrato, contrato.getNumero());
	}
	
														/*MUST FAIL*/
	
	
	@Test(expected = NotFoundException.class)
	public void TestFindByNumeroContratoMustFail() throws NotFoundException {
		Contrato contrato = null;
		String numeroContrato = "2";
		contrato = this.service.findByNumeroContrato(numeroContrato);
		Assert.assertNotNull(contrato);

	}
}
