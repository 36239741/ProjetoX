package com.br.projetox.test.service;

import java.io.FileInputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.apache.poi.util.IOUtils;
import org.directwebremoting.io.FileTransfer;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.TransactionSystemException;

import com.br.projetox.entity.Contrato;
import com.br.projetox.entity.DiaConsulta;
import com.br.projetox.entity.DiasSemana;
import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.Servico;
import com.br.projetox.entity.TipoContrato;
import com.br.projetox.exception.ImportPlanilhaException;
import com.br.projetox.repository.ContratoRepository;
import com.br.projetox.repository.PlanoContratoRepository;
import com.br.projetox.repository.ServicoRepository;
import com.br.projetox.service.ContratoService;
import com.br.projetox.service.HibernateEnversService;

import javassist.NotFoundException;

public class ContratoServiceTest extends AbstractIntegrationTest {
	
	@Autowired
	private HibernateEnversService hibernateEnversService;
	
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

	@Test
	public void importPla() throws Exception  {
		this.hibernateEnversService.findRevisionContrato();
	}
	
					/*TESTE PARA VERIFICAR O RETORNO DO MAP ,VERIFICANDO O NUMERO DE CONTRATOS SALVOS*/
		@Sql({"/dataset/truncate.sql",
			"/dataset/Servico.sql"})
	@Test
	public void importPlanilhaContratosTestMustPassVerificandoContratosSalvos() throws Exception  {
		FileInputStream fileInputStream = new FileInputStream( "/home/henrique/Documentos/GitHub/ProjetoX/Docs/PlanilhaDeDados.xlsx" );
		byte[] arquivoBytes = IOUtils.toByteArray( fileInputStream );
		HashMap<String, Integer> map = null;
		map = this.service.importPlanilhaContratos(new FileTransfer( "PlanilhaDeDados.xlsx", "xls", arquivoBytes ));
		final int saveContrato = 2;
		
		Assert.assertNotNull(map);
		Assert.assertEquals(map.get("save").intValue(), saveContrato);
	}
		
		
		
		/*TESTE QUE VERIFICA QUANTOS CONTRATOS FORAM ATUALIZADOS*/
		@Sql({"/dataset/truncate.sql",
			"/dataset/Servico.sql",
			"/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql"})
	@Test
	public void importPlanilhaContratosTestMustPassVerificandoContratosAtualizados() throws Exception  {
		FileInputStream fileInputStream = new FileInputStream( "/home/henrique/Documentos/GitHub/ProjetoX/Docs/PlanilhasDeTeste/importPlanilhaContratosTestMustPassVerificandoContratosAtualizados.xlsx" );
		byte[] arquivoBytes = IOUtils.toByteArray( fileInputStream );
		HashMap<String, Integer> map = null;
		map = this.service.importPlanilhaContratos(new FileTransfer( "importPlanilhaContratosTestMustPassVerificandoContratosAtualizados.xlsx", "xls", arquivoBytes ));
		final int updateContrato = 1;
		final int saveContrato = 1;

		
		
		Assert.assertNotNull(map);
		Assert.assertEquals(map.get("update").intValue(), updateContrato);
		Assert.assertEquals(map.get("save").intValue(), saveContrato);

	}
		/*TESTE QUE VERIFICA O CADASTRAMENTO DA BIOMETRIA*/
		@Sql({"/dataset/truncate.sql",
			"/dataset/Servico.sql",
			"/dataset/Contrato.sql",
			"/dataset/Usuario.sql",
			"/dataset/PlanoContratado.sql"})
	@Test
	public void capturandoDigitalTestMustPassCapturarDigital() throws Exception  {
		this.service.saveFingerprint("1");
		Contrato contrato = this.contratoRepository.findByNumero("1").get();
		Assert.assertNotNull(contrato.getBiometria());
	}

	/*TESTE QUE BUSCA CONTRATO COM A BIOMETRIA*/
		@Sql({"/dataset/truncate.sql",
			"/dataset/Servico.sql",
			"/dataset/Contrato.sql",
			"/dataset/Usuario.sql",
			"/dataset/PlanoContratado.sql"})
	@Test
	public void capturandoDigitalTestMustPassBuscaContratosPelaBiometria() throws Exception  {
	Contrato contrato = this.service.findByBiometria();
	Assert.assertNotNull(contrato);
	}

		/*TESTE CALCULA O VALOR DO DESCONTO E DISTRIBUI ENTRE OS PLANOS*/
			@Sql({"/dataset/truncate.sql",
				"/dataset/Servico.sql",
				"/dataset/Contrato.sql",
				"/dataset/Usuario.sql",
				"/dataset/PlanoContratado.sql"})
		@Test
		public void calcularValorDescontoMustPassCalculaOValorDoDescontoEDistribuiNosPlanosContratados() throws Exception  {
		final Double desconto = 10.0;
		this.service.calcularDesconto("1", desconto);
		Contrato contrato = this.contratoRepository.findById(1L).get();
		Assert.assertEquals(desconto ,contrato.getDesconto());
		}
		
		/*TESTE QUE VERIFICA QUANTOS CONTRATOS FORAM ATUALIZADOS*/
		@Sql({"/dataset/truncate.sql",
			"/dataset/Servico.sql",})
	@Test
	public void importPlanilhaContratosTestMustPassVerificandoPlanosContratados() throws Exception  {
		FileInputStream fileInputStream = new FileInputStream( "/home/henrique/Documentos/GitHub/ProjetoX/Docs/PlanilhasDeTeste/importPlanilhaContratosTestMustPassVerificandoPlanosContratados.xlsx" );
		byte[] arquivoBytes = IOUtils.toByteArray( fileInputStream );
		final int numerosPlanosContratados = 14;
		this.service.importPlanilhaContratos(new FileTransfer( "importPlanilhaContratosTestMustPassVerificandoPlanosContratados.xlsx", "xls", arquivoBytes ));

		List<PlanoContratado> listPlanoContratado = this.planoContratadoRepository.findAll();
		
		
		Assert.assertNotNull(listPlanoContratado);
		Assert.assertEquals(numerosPlanosContratados, listPlanoContratado.size());

	}
	
								/* TESTE PARA VERIFICAR A ATUALIZACAO DO PLANO CONTRATADO UTILIZANDO CAMPO SESSAO E O CAMPO SERVICO*/
	@Sql({	"/dataset/truncate.sql",
			"/dataset/Servico.sql",
			"/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql"})
	@Test
	public void importPlanilhaContratosTestMustPassVerificandoAtualizacaoPlanoContratado() throws Exception  {
		FileInputStream fileInputStream = new FileInputStream( "/home/henrique/Documentos/GitHub/ProjetoX/Docs/PlanilhasDeTeste/importPlanilhaContratosTestMustPassVerificandoAtualizacaoPlanoContratado.xlsx" );
		byte[] arquivoBytes = IOUtils.toByteArray( fileInputStream );
		this.service.importPlanilhaContratos(new FileTransfer( "importPlanilhaContratosTestMustPassVerificandoAtualizacaoPlanoContratado.xlsx", "xls", arquivoBytes ));
		
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
	
							/* TESTE PARA VERIFICAR A ATUALIZACAO DO PLANO CONTRATADO UTILIZANDO CAMPO ENTRADA E SAIDA PADRAO*/
	@Sql({	"/dataset/truncate.sql",
	"/dataset/Servico.sql",
	"/dataset/Contrato.sql",
	"/dataset/PlanoContratado.sql"})
	@Test
	public void importPlanilhaContratosTestMustPassVerificandoAtualizacaoPlanoContratadoPeloCampoEntradaESaida() throws Exception  {
	FileInputStream fileInputStream = new FileInputStream( "/home/henrique/Documentos/GitHub/ProjetoX/Docs/PlanilhasDeTeste/importPlanilhaContratosTestMustPassVerificandoAtualizacaoPlanoContratadoPeloCampoEntradaESaida.xlsx" );
	byte[] arquivoBytes = IOUtils.toByteArray( fileInputStream );
	this.service.importPlanilhaContratos(new FileTransfer( "importPlanilhaContratosTestMustPassVerificandoAtualizacaoPlanoContratadoPeloCampoEntradaESaida.xlsx", "xls", arquivoBytes ));
	
	final Optional<Contrato> contrato =  this.contratoRepository.findByNumero("1");
	Assert.assertNotNull(contrato.get());
	
	final int contratoId = 1;
	final LocalTime horaDeEntrada = LocalTime.parse("09:00");
	final LocalTime horaSaida = LocalTime.parse("09:40");
	final String servico = "Neuropsicopedagogia";
	final TipoContrato tipoContrato = TipoContrato.PARTICULAR;
	
	Servico objectServico = this.serviceRepository.findByServicoIgnoreCase(servico);
	
	PlanoContratado planoContratado = this.planoContratadoRepository.findPlanoContratadoAtivoByContratoAndServicoAndTipoContrato(
	objectServico.getId(),
	contratoId, 
	tipoContrato);	
	
	Assert.assertNotNull(planoContratado);
	Assert.assertEquals(horaDeEntrada,planoContratado.getHorarioEntrada());
	Assert.assertEquals(horaSaida, planoContratado.getHorarioSaida());
	
	}
	
				/* TESTE PARA VERIFICAR A ATUALIZACAO DO PLANO CONTRATADO UTILIZANDO CAPO VALOR TOTAL E CALCULANDO VALOR DO PLANO */
	@Sql({	"/dataset/truncate.sql",
	"/dataset/Servico.sql",
	"/dataset/Contrato.sql",
	"/dataset/PlanoContratado.sql"})
	@Test
	public void importPlanilhaContratosTestMustPassVerificandoAtualizacaoPlanoContratadoPeloCampoValorPlano() throws Exception  {
	FileInputStream fileInputStream = new FileInputStream( "/home/henrique/Documentos/GitHub/ProjetoX/Docs/PlanilhasDeTeste/importPlanilhaContratosTestMustPassVerificandoAtualizacaoPlanoContratadoPeloCampoValorPlano.xlsx" );
	byte[] arquivoBytes = IOUtils.toByteArray( fileInputStream );
	this.service.importPlanilhaContratos(new FileTransfer( "importPlanilhaContratosTestMustPassVerificandoAtualizacaoPlanoContratadoPeloCampoValorPlano.xlsx", "xls", arquivoBytes ));
	
	final Optional<Contrato> contrato =  this.contratoRepository.findByNumero("1");
	Assert.assertNotNull(contrato.get());
	
	final int contratoId = 1;
	final Double valorPlano = 200.00 / 4;
	final Double valorTotal = 200.00 ;
	final String servico = "Neuropsicopedagogia";
	final TipoContrato tipoContrato = TipoContrato.PARTICULAR;
	
	Servico objectServico = this.serviceRepository.findByServicoIgnoreCase(servico);
	
	PlanoContratado planoContratado = this.planoContratadoRepository.findPlanoContratadoAtivoByContratoAndServicoAndTipoContrato(
	objectServico.getId(),
	contratoId, 
	tipoContrato);	
	
	Assert.assertNotNull(planoContratado);
	Assert.assertEquals(valorPlano,planoContratado.getValorPlano());
	Assert.assertEquals(valorTotal, planoContratado.getValorTotal());
	
	}
	
		/* TESTE PARA VERIFICAR A ATUALIZACAO DO PLANO CONTRATADO UTILIZANDO CAMPO DIAS SEMANA */
	@Sql({	"/dataset/truncate.sql",
	"/dataset/Servico.sql",
	"/dataset/Contrato.sql",
	"/dataset/PlanoContratado.sql",
	"/dataset/Usuario.sql"})
	@Test
	public void importPlanilhaContratosTestMustPassVerificandoAtualizacaoPlanoContratadoPeloCampoDiasSemana() throws Exception  {
	FileInputStream fileInputStream = new FileInputStream( "/home/henrique/Documentos/GitHub/ProjetoX/Docs/PlanilhasDeTeste/importPlanilhaContratosTestMustPassVerificandoAtualizacaoPlanoContratadoPeloCampoDiasSemana.xlsx" );
	byte[] arquivoBytes = IOUtils.toByteArray( fileInputStream );
	this.service.importPlanilhaContratos(new FileTransfer( "importPlanilhaContratosTestMustPassVerificandoAtualizacaoPlanoContratadoPeloCampoDiasSemana.xlsx", "xls", arquivoBytes ));
	
	final Optional<Contrato> contrato =  this.contratoRepository.findByNumero("1");
	Assert.assertNotNull(contrato.get());
	final List<DiaConsulta> diasSemana = new ArrayList<DiaConsulta>();
	DiaConsulta dia1 = new DiaConsulta();
	dia1.setDiasSemana(DiasSemana.SEGUNDA);
	DiaConsulta dia2 = new DiaConsulta();
	dia2.setDiasSemana(DiasSemana.TERCA);
	diasSemana.add(dia1);
	diasSemana.add(dia2);
	final int contratoId = 1;
	
	final String servico = "Neuropsicopedagogia";
	final TipoContrato tipoContrato = TipoContrato.PARTICULAR;
	
	Servico objectServico = this.serviceRepository.findByServicoIgnoreCase(servico);
	
	PlanoContratado planoContratado = this.planoContratadoRepository.findPlanoContratadoAtivoByContratoAndServicoAndTipoContrato(
	objectServico.getId(),
	contratoId, 
	tipoContrato);	
	
	
	Assert.assertNotNull(planoContratado);
	Assert.assertEquals(diasSemana.toString(), planoContratado.getDiaConsulta().toString());

	
	}
	/* TESTE PARA VERIFICAR A ATUALIZACAO DO PLANO CONTRATADO REMOVENDO DIAS DA SEMANA*/
	@Sql({	"/dataset/truncate.sql",
	"/dataset/Servico.sql",
	"/dataset/Contrato.sql",
	"/dataset/PlanoContratado.sql",
	"/dataset/Usuario.sql"})
	@Test
	public void importPlanilhaContratosTestMustPassVerificandoAtualizacaoPlanoContratadoRemovendoDiaDaSemana() throws Exception  {
	FileInputStream fileInputStream = new FileInputStream( "/home/henrique/Documentos/GitHub/ProjetoX/Docs/PlanilhasDeTeste/importPlanilhaContratosTestMustPassVerificandoAtualizacaoPlanoContratadoRemovendoDiaDaSemana.xlsx" );
	byte[] arquivoBytes = IOUtils.toByteArray( fileInputStream );
	this.service.importPlanilhaContratos(new FileTransfer( "importPlanilhaContratosTestMustPassVerificandoAtualizacaoPlanoContratadoRemovendoDiaDaSemana.xlsx", "xls", arquivoBytes ));
	
	final Optional<Contrato> contrato =  this.contratoRepository.findByNumero("2");
	Assert.assertNotNull(contrato.get());
	final List<DiaConsulta> diasSemana = new ArrayList<DiaConsulta>();
	DiaConsulta dia1 = new DiaConsulta();
	dia1.setDiasSemana(DiasSemana.SEGUNDA);
	diasSemana.add(dia1);
	final String servico = "Fonoaudiologia";
	final TipoContrato tipoContrato = TipoContrato.PLANO;
	
	Servico objectServico = this.serviceRepository.findByServicoIgnoreCase(servico);
	
	PlanoContratado planoContratado = this.planoContratadoRepository.findPlanoContratadoAtivoByContratoAndServicoAndTipoContrato(
	objectServico.getId(),
	contrato.get().getId(), 
	tipoContrato);	
	
	
	Assert.assertNotNull(planoContratado);
	Assert.assertEquals(diasSemana.toString(), planoContratado.getDiaConsulta().toString());

	
	}
		/* TESTE PARA VERIFICAR PARA VERIFICAR A PERFORMACE DE IMPORTACAO DA PLANILHA */
	@Sql({	"/dataset/truncate.sql",
	"/dataset/Servico.sql",
	"/dataset/Contrato.sql",
	"/dataset/Usuario.sql",
	"/dataset/PlanoContratado.sql"})
	@Test
	public void importPlanilhaContratosTestMustPassVerificandoPerformaceDeImporatacao() throws Exception  {
	FileInputStream fileInputStream = new FileInputStream( "/home/henrique/Documentos/GitHub/ProjetoX/Docs/PlanilhasDeTeste/importPlanilhaContratosTestMustPassVerificandoPerformaceDeImporatacao.xlsx" );
	byte[] arquivoBytes = IOUtils.toByteArray( fileInputStream );
	this.service.importPlanilhaContratos(new FileTransfer( "importPlanilhaContratosTestMustPassVerificandoPerformaceDeImporatacao.xlsx", "xls", arquivoBytes ));
	final int numerosContratosInseridos = 11;
	
	List<Contrato> list = this.contratoRepository.findAll();
	Assert.assertNotNull(list);
	Assert.assertEquals(numerosContratosInseridos, list.size());
	
	}
	/*TESTE QUE VERIFICA O TIPO DE RETORNO MISTO DO ATRIBUTO planoContratadoTransient*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({	"/dataset/truncate.sql",
			"/dataset/Usuario.sql",
			"/dataset/Servico.sql",
			"/dataset/Contrato.sql",
			"/dataset/Config.sql",
			"/dataset/PlanoContratado.sql"})
	@Test
	public void ContratoMustPassVerificandoRetornoTipoPlanoCotratadoTransientTipoMisto() throws NotFoundException {
		final TipoContrato tipoContrato = TipoContrato.MISTO;
		
		final Optional<Contrato> contrato = this.contratoRepository.findByNumero("1");
		
		Assert.assertNotNull(contrato);
		Assert.assertEquals(tipoContrato, contrato.get().getTipoContratoTransient());
	}
	/*TESTE QUE VERIFICA SE O IMPORT ATUALIZA A LISTA DE PLANOS CONTRATADOS QUANDO JA EXISTE O CONTRATO*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({	"/dataset/truncate.sql",
			"/dataset/Usuario.sql",
			"/dataset/Servico.sql",
			"/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql"})
	@Test
	public void ContratoMustPassVerificandoAdicionandoPlanoContratadoEmContratoExistene() throws Exception {
		FileInputStream fileInputStream = new FileInputStream( "/home/henrique/Documentos/GitHub/ProjetoX/Docs/PlanilhasDeTeste/ContratoMustPassVerificandoAdicionandoPlanoContratadoEmContratoExistene.xlsx" );
		byte[] arquivoBytes = IOUtils.toByteArray( fileInputStream );
		this.service.importPlanilhaContratos(new FileTransfer( "ContratoMustPassVerificandoAdicionandoPlanoContratadoEmContratoExistene.xlsx", "xls", arquivoBytes ));
		PlanoContratado planoContratado = this.planoContratadoRepository.findPlanoContratadoAtivoByContratoAndServicoAndTipoContrato(
				this.serviceRepository.findByServicoIgnoreCase("Psicologia").getId(), 1L , TipoContrato.PLANO);
		
		Assert.assertNotNull(planoContratado);
		

	}
	


	
														/*MUST FAIL*/

		/* TESTE PARA VERIFICAR A FALHA QUE OCORRE QUANDO A PLANILHA ESTA INCOMPLETA FALTANDO DIAS DA SEMANA */
	@Sql({	"/dataset/truncate.sql",
	"/dataset/Servico.sql",
	"/dataset/Contrato.sql",
	"/dataset/PlanoContratado.sql"})
	@Test(expected = ImportPlanilhaException.class)
	@Rollback(false)
	public void importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletaDiasSemanaFaltando() throws Exception  {
	FileInputStream fileInputStream = new FileInputStream( "/home/henrique/Documentos/GitHub/ProjetoX/Docs/PlanilhasDeTeste/importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletaDiasSemanaFaltando.xlsx" );
	byte[] arquivoBytes = IOUtils.toByteArray( fileInputStream );
	this.service.importPlanilhaContratos(new FileTransfer( "importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletaDiasSemanaFaltando.xlsx", "xls", arquivoBytes ));

}
		/* TESTE PARA VERIFICAR A FALHA QUE OCORRE QUANDO A PLANILHA ESTA INCOMPLETA FALTANDO HORARIO DE SAIDA PADRAO */
	@Sql({	"/dataset/truncate.sql",
	"/dataset/Servico.sql",
	"/dataset/Contrato.sql",
	"/dataset/PlanoContratado.sql"})
	@Test(expected = ImportPlanilhaException.class)
	@Rollback(false)
	public void importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletasFaltandoSaidaPadrao() throws Exception  {
	FileInputStream fileInputStream = new FileInputStream( "/home/henrique/Documentos/GitHub/ProjetoX/Docs/PlanilhasDeTeste/importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletasFaltandoSaidaPadrao.xlsx" );
	byte[] arquivoBytes = IOUtils.toByteArray( fileInputStream );
	this.service.importPlanilhaContratos(new FileTransfer( "importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletasFaltandoSaidaPadrao.xlsx", "xls", arquivoBytes ));
	
	}
		/* TESTE PARA VERIFICAR A FALHA QUE OCORRE QUANDO A PLANILHA ESTA INCOMPLETA FALTANDO VALOR DO PLANO */
	@Sql({	"/dataset/truncate.sql",
	"/dataset/Servico.sql",
	"/dataset/Contrato.sql",
	"/dataset/PlanoContratado.sql"})
	@Test(expected = ImportPlanilhaException.class)
	@Rollback(false)
	public void importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletasFaltandoValorDoPlano() throws Exception  {
	FileInputStream fileInputStream = new FileInputStream( "/home/henrique/Documentos/GitHub/ProjetoX/Docs/PlanilhasDeTeste/importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletasFaltandoSaidaPadrao.xlsx" );
	byte[] arquivoBytes = IOUtils.toByteArray( fileInputStream );
	this.service.importPlanilhaContratos(new FileTransfer( "importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletasFaltandoValorDoPlano.xlsx", "xls", arquivoBytes ));
	
	}
		/* TESTE PARA VERIFICAR A FALHA QUE OCORRE QUANDO A PLANILHA ESTA INCOMPLETA FALTANDO NOME PACIENTE */
	@Sql({	"/dataset/truncate.sql",
	"/dataset/Servico.sql",
	"/dataset/Contrato.sql",
	"/dataset/PlanoContratado.sql"})
	@Test()
	@Rollback(false)
	public void importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletasFaltandoNomePaciente() throws Exception  {
	FileInputStream fileInputStream = new FileInputStream( "/home/henrique/Documentos/GitHub/ProjetoX/Docs/PlanilhasDeTeste/importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletasFaltandoNomePacient.xlsx" );
	byte[] arquivoBytes = IOUtils.toByteArray( fileInputStream );
	final String constraintError = "ConstraintViolationException";
	String constraintViolaton = null;
		try {
			this.service.importPlanilhaContratos(new FileTransfer( "importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletasFaltandoNomePaciente.xlsx", "xls", arquivoBytes ));
		}catch(TransactionSystemException e){
			constraintViolaton = e.getRootCause().getClass().getSimpleName();
		}
		Assert.assertEquals(constraintError, constraintViolaton);
		
		 
	}
		/*TESTE CALCULA O VALOR DO DESCONTO SEM PLANO */
		@Sql({"/dataset/truncate.sql",
			"/dataset/Servico.sql",
			"/dataset/Contrato.sql",
			"/dataset/Usuario.sql",
			"/dataset/PlanoContratado.sql"})
	@Test(expected = NotFoundException.class)
	public void calcularValorDescontoMustFailContratoSemPlano() throws Exception  {
	final Double desconto = 10.0;
	this.service.calcularDesconto("3", desconto);
	Contrato contrato = this.contratoRepository.findById(1L).get();
	Assert.assertEquals(desconto ,contrato.getDesconto());
	}
		
		/*TESTE CALCULA O VALOR DO DESCONTO E DISTRIBUI ENTRE OS PLANOS*/
		@Sql({"/dataset/truncate.sql",
			"/dataset/Servico.sql",
			"/dataset/Contrato.sql",
			"/dataset/Usuario.sql",
			"/dataset/PlanoContratado.sql"})
	@Test(expected = Exception.class)
	public void calcularValorDescontoMustFailComDescontoValorNulo() throws Exception  {
	final Double desconto = 0.0;
	this.service.calcularDesconto("1", desconto);
	Contrato contrato = this.contratoRepository.findById(1L).get();
	Assert.assertEquals(desconto ,contrato.getDesconto());
	}
}
