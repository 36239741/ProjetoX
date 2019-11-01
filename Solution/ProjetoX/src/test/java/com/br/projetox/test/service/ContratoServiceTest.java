package com.br.projetox.test.service;

import java.io.FileInputStream;
import java.util.List;
import java.util.Optional;

import org.apache.poi.util.IOUtils;
import org.directwebremoting.io.FileTransfer;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;

import com.br.projetox.entity.Contrato;
import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.Servico;
import com.br.projetox.entity.TipoContrato;
import com.br.projetox.repository.ContratoRepository;
import com.br.projetox.repository.PlanoContratoRepository;
import com.br.projetox.repository.ServicoRepository;
import com.br.projetox.service.ContratoService;

import javassist.NotFoundException;

public class ContratoServiceTest extends AbstractIntegrationTest {
	
	@Autowired
	private ContratoService service;
	
	@Autowired
	private ContratoRepository contratoRepository;
	
	@Autowired
	private PlanoContratoRepository planoContratadoRepository;
	
	@Autowired
	private ServicoRepository serviceRepository;
	
	
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
		FileInputStream fileInputStream = new FileInputStream( "/home/henrique/Documentos/GitHub/ProjetoX/Docs/PlanilhaDeDados.xlsx" );
		byte[] arquivoBytes = IOUtils.toByteArray( fileInputStream );
		
		this.service.importPlanilhaContratos(new FileTransfer( "PlanilhaDeDados.xlsx", "xls", arquivoBytes ));
		
		List<Contrato> contratos = contratoRepository.findAll();
		
		Assert.assertNotNull(contratos);
		Assert.assertEquals(2, contratos.size());
	}
	
								/* TESTE PARA VERIFICAR A ATUALIZACAO DO PLANO CONTRATADO UTILIZANDO CAMPO SESSAO*/
	@Sql({	"/dataset/truncate.sql",
			"/dataset/Servico.sql",
			"/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql"})
	@Test
	public void importPlanilhaContratosTestMustPassVerificandoAtualizacaoPlanoContratado() throws Exception  {
		FileInputStream fileInputStream = new FileInputStream( "/home/henrique/Documentos/GitHub/ProjetoX/Docs/PlanilhaTest.xlsx" );
		byte[] arquivoBytes = IOUtils.toByteArray( fileInputStream );
		this.service.importPlanilhaContratos(new FileTransfer( "PlanilhaDeDados.xlsx", "xls", arquivoBytes ));
		
		final Optional<Contrato> contrato =  this.contratoRepository.findByNumero("1");
		Assert.assertNotNull(contrato.get());
		
		final int contratoId = 1;
		final int numeroSessao = 4;
		final String servico = "Neuropsicopedagogia";
		final TipoContrato tipoContrato = TipoContrato.PARTICULAR;
		
		Servico objectServico = this.serviceRepository.findByServicoIgnoreCase(servico);
			
		PlanoContratado planoContratado = this.planoContratadoRepository.findPlanoContratadoAtivoByContratoAndServicoAndTipoContrato(
				objectServico.getId(),
				contratoId, 
				tipoContrato);	
		
		Assert.assertNotNull(planoContratado);
		Assert.assertEquals(numeroSessao, planoContratado.getSessao());
		
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
		contrato = this.service.findByContractNumber(numeroContrato);
		Assert.assertNotNull(contrato);
		Assert.assertEquals(numeroContrato, contrato.getNumero());
	}
	
														/*MUST FAIL*/
	
	
	@Test(expected = NotFoundException.class)
	public void TestFindByNumeroContratoMustFail() throws NotFoundException {
		Contrato contrato = null;
		String numeroContrato = "2";
		contrato = this.service.findByContractNumber(numeroContrato);
		Assert.assertNotNull(contrato);

	}
}
