package com.br.projetox.test.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import org.directwebremoting.io.FileTransfer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.br.projetox.service.ContratoService;



public class TestPlanilha extends AbstractIntegrationTest {
	@Autowired
	private ContratoService contratoService;
	
	@Test
	public void importPlanilhaContratosTestMustPass() throws Exception  {
		this.contratoService.importPlanilhaContratos();
		
	}
}
