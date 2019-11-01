package com.br.projetox.test.service;

import java.io.FileInputStream;
import java.io.FileReader;
import java.util.List;

import org.apache.poi.util.IOUtils;
import org.directwebremoting.io.FileTransfer;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;

import com.br.projetox.entity.Contrato;
import com.br.projetox.repository.ContratoRepository;
import com.br.projetox.service.ContratoService;

import javassist.NotFoundException;

public class ContratoServiceTest extends AbstractIntegrationTest {
	
	@Autowired
	private ContratoService service;
	
	@Autowired
	private ContratoRepository contratoRepository;
	
	
	/*
	 * ==================== TESTES DE IMPORTAÇÃO DA PLANILHA ==============================
	 */
	
	/*Fazer testes que verifiquem:
	# quantos contratos foram criados, tanto pelo map de retorno como pelo repositório
	# quantos contratos foram atualizados, tanto pelo map de retorno como pelo repositório
	# quantos planos há após import da planilha
	# erros diversos, como: planilha mal preenchida, sem alguns valores importantes por exemplo
	*/
	
	@Sql({	"/dataset/truncate.sql",
		"/dataset/Servico.sql"})
	@Test
	public void importPlanilhaContratosTestMustPassVerificandoContratos() throws Exception  {
		FileInputStream fileInputStream = new FileInputStream( "/Users/marcielilanger/Documents/IFPR/ProjetoHenrique/ProjetoX/Docs/PlanilhaDeDados.xlsx" );
		byte[] arquivoBytes = IOUtils.toByteArray( fileInputStream );
		
		this.service.importPlanilhaContratos(new FileTransfer( "PlanilhaDeDados.xlsx", "xls", arquivoBytes ));
		
		List<Contrato> contratos = contratoRepository.findAll();
		
		Assert.assertNotNull(contratos);
		Assert.assertEquals(2, contratos.size());
	}
	
	/*
	 * ==================== TESTES DE .... ==============================
	 */
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
