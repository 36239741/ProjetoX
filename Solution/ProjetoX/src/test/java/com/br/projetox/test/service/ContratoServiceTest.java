package com.br.projetox.test.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.validation.ValidationException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.util.IOUtils;
import org.directwebremoting.io.FileTransfer;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import com.br.projetox.exception.ContratoException;
import com.br.projetox.exception.ImportPlanilhaException;
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
	 * ==================== TESTES DE IMPORTAÇÃO DA PLANILHA
	 * ==============================
	 */

	/*
	 * Fazer testes que verifiquem: # erros diversos, como: planilha mal preenchida,
	 * sem alguns valores importantes por exemplo
	 */

/* Teste que consulta um contrato pela biometria*/
	
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
		"/dataset/PlanoContratado.sql","/dataset/RegistroTestExportPlanilha.sql","/dataset/Config.sql" })
	@Test
	@WithUserDetails("henrique_nitatori@hotmail.com")
	public void consultarContratoPelaBiometriaTestMustPass() throws Exception {
		Contrato contrato = this.service.consultarContratosPorBiometria();
		Assert.assertNotNull(contrato);
		Assert.assertEquals("Henrique Nitatori", contrato.getNomePaciente());
	}
	
/* Teste salva uma biometria em um contrato*/
	
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
		"/dataset/PlanoContratado.sql","/dataset/RegistroTestExportPlanilha.sql","/dataset/Config.sql" })
	@Test
	@WithUserDetails("henrique_nitatori@hotmail.com")
	public void salvarBiometriaTestMustPass() throws Exception {
		this.service.salvarBiometria("1");
	}
	
	
	
/* Teste que para verifica a consulta do relatorio mensal de um contrato com 0 registros*/
	
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
		"/dataset/PlanoContratado.sql","/dataset/RegistroTestExportPlanilha.sql","/dataset/Config.sql" })
	@Test
	@WithUserDetails("henrique_nitatori@hotmail.com")
	public void consultarRelatorioMensalTestMustPassConsultandoRelatorioMensalComZeroRegistros() throws Exception {
		List<Contrato> contratos = this.service.consultarRelatorioMensal(2, 2020);
		
		Assert.assertNotNull(contratos);
		Assert.assertEquals(0, contratos.get(0).getRegistro().size());
	}
	
/* Teste que para verifica a consulta do relatorio mensal de um contrato*/
	
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
		"/dataset/PlanoContratado.sql","/dataset/RegistroTestExportPlanilha.sql","/dataset/Config.sql" })
	@Test
	@WithUserDetails("henrique_nitatori@hotmail.com")
	public void consultarRelatorioMensalTestMustPass() throws Exception {
		final Double valorExecutado = 1090.00;
		List<Contrato> contratos = this.service.consultarRelatorioMensal(3, 2020);
		
		Assert.assertNotNull(contratos);
		Assert.assertEquals(3, contratos.get(0).getRegistro().size());
		Assert.assertEquals(valorExecutado, contratos.get(0).getValorExecutado());
	}
	
/* Teste que para verificar o exportar da planilha de registro mensal*/
	
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
		"/dataset/PlanoContratado.sql","/dataset/RegistroTestExportPlanilha.sql","/dataset/Config.sql" })
	@Test
	@WithUserDetails("henrique_nitatori@hotmail.com")
	public void exportarPlanilhaRelatorioTestMustPass() throws Exception {
		ByteArrayOutputStream bytePLanilha = this.service.exportarPlanilhaRelatorio(2020,3);
		File file = new File("teste.xlsx");
		FileOutputStream createPlanilha = new FileOutputStream(file);
		createPlanilha.write(bytePLanilha.toByteArray());
		createPlanilha.flush();
		createPlanilha.close();
	}
	
	/* Teste que verifica o import dos contratos atraves de uma planilha xlsx*/
	
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql" })
	@Test
	@WithUserDetails("henrique_nitatori@hotmail.com")
	public void importarPlanilhaDeContratosTestMustPassImportandoContratosAtravesPlanilhaXlsx() throws Exception {
		final String nomePaciente = "Henrique nitatori";
		final String numeroContrato = "1";
		final int numeroPlanoContratados = 3;
		
		String basePath = new File("").getAbsolutePath();
		FileInputStream fileInputStream = new FileInputStream(
				basePath + "/src/test/resources/PlanilhasDeTeste/PlanilhaDeDados.xlsx");
		byte[] arquivoBytes = IOUtils.toByteArray(fileInputStream);
		this.service.importarPlanilhaContratos(new FileTransfer("PlanilhaDeDados.xlsx", "xls", arquivoBytes));

		List<Contrato> contratos = contratoRepository.findAll();
		List<PlanoContratado> planos = this.planoContratadoRepository.consultarPlanoContratadoPorNumeroContrato(numeroContrato);
		
		Assert.assertNotNull(contratos);
		Assert.assertEquals(2, contratos.size());
		Assert.assertEquals(nomePaciente, contratos.get(0).getNomePaciente());
		Assert.assertEquals(numeroContrato, contratos.get(0).getNumero());
		Assert.assertEquals(numeroPlanoContratados, planos.size());
	}
	
	/* Teste que verifica os dados de dias semana vindo do import*/
	
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql" })
	@Test
	@WithUserDetails("henrique_nitatori@hotmail.com")
	public void importarPlanilhaDeContratosTestMustPassVerificandoDiasSemanaVindoImport() throws Exception {
		final DiasSemana segunda = DiasSemana.SEGUNDA;
		final DiasSemana terca = DiasSemana.TERCA;
		
		String basePath = new File("").getAbsolutePath();
		FileInputStream fileInputStream = new FileInputStream(
				basePath + "/src/test/resources/PlanilhasDeTeste/PlanilhaDeDados.xlsx");
		byte[] arquivoBytes = IOUtils.toByteArray(fileInputStream);
		this.service.importarPlanilhaContratos(new FileTransfer("PlanilhaDeDados.xlsx", "xls", arquivoBytes));

		List<PlanoContratado> planos = this.planoContratadoRepository.consultarPlanoContratadoPorNumeroContrato("1");
		Assert.assertEquals(segunda, planos.get(1).getDiaConsulta().get(0).getDiasSemana());
		Assert.assertEquals(terca, planos.get(1).getDiaConsulta().get(1).getDiasSemana());
	}
	
	/* Teste que verifica os dados do contrato que foram persistidos no banco*/
	
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql" })
	@Test
	@WithUserDetails("henrique_nitatori@hotmail.com")
	public void importarPlanilhaDeContratosTestMustPassVerificandoAsIformacoesDoContratoPersitida() throws Exception {
		Contrato contrato = new Contrato();
		contrato.setNomePaciente("Henrique nitatori");
		contrato.setNumero("1");
		contrato.setValorTotal(740.00);
		String basePath = new File("").getAbsolutePath();
		FileInputStream fileInputStream = new FileInputStream(
				basePath + "/src/test/resources/PlanilhasDeTeste/PlanilhaDeDados.xlsx");
		byte[] arquivoBytes = IOUtils.toByteArray(fileInputStream);
		this.service.importarPlanilhaContratos(new FileTransfer("PlanilhaDeDados.xlsx", "xls", arquivoBytes));
		Contrato consultarContrato = this.contratoRepository.findByNumero("1").get();
		Assert.assertEquals(contrato.getNomePaciente(), consultarContrato.getNomePaciente());
		Assert.assertEquals(contrato.getNumero(),consultarContrato.getNumero());
		Assert.assertEquals(contrato.getValorTotal(), consultarContrato.getValorTotal());
	}
	/* Teste que verifica os dados do planoContrato que foram persistidos no banco*/
	
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql" })
	@Test
	@WithUserDetails("henrique_nitatori@hotmail.com")
	public void importarPlanilhaDeContratosTestMustPassVerificandoAsIformacoesDoPlanoContratoPersitida() throws Exception {
		PlanoContratado plano  = new PlanoContratado();
		DiaConsulta diaConsulta = new DiaConsulta();
		diaConsulta.setDiasSemana(DiasSemana.SEGUNDA);
		plano.getDiaConsulta().add(diaConsulta);
		LocalTime horarioEntrada = LocalTime.parse("09:20:00");
		plano.setHorarioEntrada(horarioEntrada);
		LocalTime horarioSaida = LocalTime.parse("10:00:00");
		plano.setHorarioSaida(horarioSaida);
		Servico servico = new Servico();
		servico.setServico("Neuropsicopedagogia");
		plano.setServico(servico);
		plano.setSessao(5);
		plano.setTipoContrato(TipoContrato.PARTICULAR);
		plano.setValorTotal(100.00);
		plano.setValorAtendimento(100.00);
		plano.setValorSessao(20.00);
		String basePath = new File("").getAbsolutePath();
		FileInputStream fileInputStream = new FileInputStream(
				basePath + "/src/test/resources/PlanilhasDeTeste/PlanilhaDeDados.xlsx");
		byte[] arquivoBytes = IOUtils.toByteArray(fileInputStream);
		this.service.importarPlanilhaContratos(new FileTransfer("PlanilhaDeDados.xlsx", "xls", arquivoBytes));
		PlanoContratado consultaPlano = this.planoContratadoRepository.consultarPlanoContratadoPorNumeroContrato("1").get(0);
		Assert.assertEquals(plano.getDiaConsulta().get(0).getDiasSemana(), consultaPlano.getDiaConsulta().get(0).getDiasSemana());
		Assert.assertEquals(plano.getHorarioEntrada(), consultaPlano.getHorarioEntrada());
		Assert.assertEquals(plano.getHorarioSaida(), consultaPlano.getHorarioSaida());
		Assert.assertEquals(plano.getServico(), plano.getServico());
		Assert.assertEquals(plano.getSessao(), consultaPlano.getSessao());
		Assert.assertEquals(plano.getTipoContrato(), consultaPlano.getTipoContrato());
		Assert.assertEquals(plano.getValorTotal(), consultaPlano.getValorTotal());
		Assert.assertEquals(plano.getValorAtendimento(), consultaPlano.getValorAtendimento());
		Assert.assertEquals(plano.getValorSessao(), consultaPlano.getValorSessao());
	}
	
	/* Teste que verifica os dados do contratos que foram atualizados*/
	
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql" })
	@Test
	@WithUserDetails("henrique_nitatori@hotmail.com")
	public void importarPlanilhaDeContratosTestMustPassVerificandoAsIformacoesDoContratoAtualizadas() throws Exception {
		final String nomePaciente = "Henrique dos santos";
		String basePath = new File("").getAbsolutePath();
		FileInputStream fileInputStream = new FileInputStream(
				basePath + "/src/test/resources/PlanilhasDeTeste/ContratoMustPassVerificandoAdicionandoPlanoContratadoEmContratoExistene.xlsx");
		byte[] arquivoBytes = IOUtils.toByteArray(fileInputStream);
		this.service.importarPlanilhaContratos(new FileTransfer("ContratoMustPassVerificandoAdicionandoPlanoContratadoEmContratoExistene.xlsx", "xls", arquivoBytes));
		Contrato contrato = this.contratoRepository.findByNumero("1").get();
		
		Assert.assertEquals(nomePaciente, contrato.getNomePaciente());
	}
	
	/* Teste que verifica os dados do PlanoContratado que foram atualizados*/
	
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
		"/dataset/PlanoContratado.sql","/dataset/RegistroTestExportPlanilha.sql","/dataset/Config.sql" })
	@Test
	@WithUserDetails("henrique_nitatori@hotmail.com")
	public void importarPlanilhaDeContratosTestMustPassVerificandoAsIformacoesDoPlanoContratadoAtualizadas() throws Exception {
		PlanoContratado plano  = new PlanoContratado();
		DiaConsulta diaConsulta = new DiaConsulta();
		diaConsulta.setDiasSemana(DiasSemana.SEGUNDA);
		plano.getDiaConsulta().add(diaConsulta);
		LocalTime horarioEntrada = LocalTime.parse("09:20:00");
		plano.setHorarioEntrada(horarioEntrada);
		LocalTime horarioSaida = LocalTime.parse("10:00:00");
		plano.setHorarioSaida(horarioSaida);
		Servico servico = new Servico();
		servico.setServico("Neuropsicopedagogia");
		plano.setServico(servico);
		plano.setSessao(4);
		plano.setTipoContrato(TipoContrato.PARTICULAR);
		plano.setValorTotal(100.00);
		plano.setValorAtendimento(100.00);
		plano.setValorSessao(25.00);
		String basePath = new File("").getAbsolutePath();
		FileInputStream fileInputStream = new FileInputStream(
				basePath + "/src/test/resources/PlanilhasDeTeste/ContratoMustPassVerificandoAdicionandoPlanoContratadoEmContratoExistene.xlsx");
		byte[] arquivoBytes = IOUtils.toByteArray(fileInputStream);
		this.service.importarPlanilhaContratos(new FileTransfer("ContratoMustPassVerificandoAdicionandoPlanoContratadoEmContratoExistene.xlsx", "xls", arquivoBytes));
		
		PlanoContratado consultaPlano = this.planoContratadoRepository
				.consultarPlanoContratadoAtivoPorServiceIdContratoIdTipoContrato(4, "1", TipoContrato.PARTICULAR);
		
		Assert.assertEquals(plano.getHorarioEntrada(), consultaPlano.getHorarioEntrada());
		Assert.assertEquals(plano.getHorarioSaida(), consultaPlano.getHorarioSaida());
		Assert.assertEquals(plano.getServico(), plano.getServico());
		Assert.assertEquals(plano.getSessao(), consultaPlano.getSessao());
		Assert.assertEquals(plano.getTipoContrato(), consultaPlano.getTipoContrato());
		Assert.assertEquals(plano.getValorTotal(), consultaPlano.getValorTotal());
		Assert.assertEquals(plano.getValorAtendimento(), consultaPlano.getValorAtendimento());
		Assert.assertEquals(plano.getValorSessao(), consultaPlano.getValorSessao());
		
	}
	
	
	/* Teste que verifica o retorno do numero de contratos salvos*/
	
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql" })
	@Test
	@WithUserDetails("henrique_nitatori@hotmail.com")
	public void importarPlanilhaDeContratosTestMustPassVerificandoRetornoDeContratosSalvos() throws Exception {
		final Integer totalContratosSalvos = 2;
		
		String basePath = new File("").getAbsolutePath();
		FileInputStream fileInputStream = new FileInputStream(
				basePath + "/src/test/resources/PlanilhasDeTeste/PlanilhaDeDados.xlsx");
		byte[] arquivoBytes = IOUtils.toByteArray(fileInputStream);
		HashMap<String, Integer> contratosSalvos = this.service.importarPlanilhaContratos(new FileTransfer("PlanilhaDeDados.xlsx", "xls", arquivoBytes));

		Assert.assertEquals(totalContratosSalvos, contratosSalvos.get("salvo"));
		
	}
	
	/* Teste que verifica o retorno do numero de contratos atualizados*/
	
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
		"/dataset/Config.sql", "/dataset/PlanoContratado.sql" })
	@Test
	@WithUserDetails("henrique_nitatori@hotmail.com")
	public void importarPlanilhaDeContratosTestMustPassVerificandoRetornoDeContratosAtualizados() throws Exception {
		final Integer totalContratosSalvos = 2;
		
		String basePath = new File("").getAbsolutePath();
		FileInputStream fileInputStream = new FileInputStream(
				basePath + "/src/test/resources/PlanilhasDeTeste/PlanilhaDeDados.xlsx");
		byte[] arquivoBytes = IOUtils.toByteArray(fileInputStream);
		HashMap<String, Integer> contratosSalvos = this.service.importarPlanilhaContratos(new FileTransfer("PlanilhaDeDados.xlsx", "xls", arquivoBytes));

		Assert.assertEquals(totalContratosSalvos, contratosSalvos.get("atualizado"));
		
	}

	
	/* Teste que busca todos contratos e verifica a quantidade retornada */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/RegistroTestExportPlanilha.sql","/dataset/Config.sql" })
	@Test
	public void consultarContratosMustPassBuscandoTodosContratos() throws Exception {
		final int quantidadeContratosSalvos = 3;
		
		
		Page<Contrato> pageContrato = this.service.consultarTodosContratos("", "", true, PageRequest.of(0, 10));
		
		Assert.assertNotNull(pageContrato.getContent());
		Assert.assertEquals(quantidadeContratosSalvos, pageContrato.getContent().size());
		
	}
	
	/* Teste que verifica o calculo do valor executado de um contrato */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/RegistroTestExportPlanilha.sql" })
	@Test
	public void consultarContratosMustPassTestandoOCalculoValorExecutado() throws Exception {
		final Double valorExecutado = 1090.00;
		
		
		Page<Contrato> pageContrato = this.service.consultarTodosContratos("", "", true, PageRequest.of(0, 10));
		
		
		Assert.assertNotNull(pageContrato.getContent());
		Assert.assertEquals(valorExecutado, pageContrato.getContent().get(0).getValorExecutado());
		
	}
	
	/* Teste que verifica a busca de um contrato pelo filtro nomePaciente */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/RegistroTestExportPlanilha.sql" })
	@Test
	public void consultarContratosMustPassConsultandoContratoPeloFiltroNomePaciente() throws Exception {
		final String nomePaciente = "Henrique Nitatori";
		
		
		Page<Contrato> pageContrato = this.service.consultarTodosContratos("", "Henrique Nitatori", true, PageRequest.of(0, 10));
		
		
		Assert.assertNotNull(pageContrato.getContent());
		Assert.assertEquals(nomePaciente, pageContrato.getContent().get(0).getNomePaciente());
		
	}
	
	/* Teste que verifica a busca de um contrato pelo filtro do numeroContrato */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/RegistroTestExportPlanilha.sql" })
	@Test
	public void consultarContratosMustPassConsultandoContratoPeloFiltroNumero() throws Exception {
		final String numeroContrato = "1";
		
		
		Page<Contrato> pageContrato = this.service.consultarTodosContratos("1", "", true, PageRequest.of(0, 10));
		
		
		Assert.assertNotNull(pageContrato.getContent());
		Assert.assertEquals(numeroContrato, pageContrato.getContent().get(0).getNumero());
		
	}
	/* Teste que verifica o calculo da diferenca entre valor total do contrato e valor executado */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/RegistroTestExportPlanilha.sql" })
	@Test
	public void consultarContratosMustPassVerificandoOCalculoDaDiferenca() throws Exception {
		final Double diferenca = 90.00;
		
		
		Page<Contrato> pageContrato = this.service.consultarTodosContratos("", "", true, PageRequest.of(0, 10));
		
		
		Assert.assertNotNull(pageContrato.getContent());
		Assert.assertEquals(diferenca, pageContrato.getContent().get(0).getDiferenca());
		
	}
	
	/* Teste que verifica a consulta de um contrato pelo numero do contrato*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/RegistroTestExportPlanilha.sql" })
	@Test
	public void consultarContratoMustPassConsultandoContratoEspecifico() throws Exception {
		final String numeroContrato = "1";
		final String nomePaciente = "Henrique Nitatori";
		final int numeroPlanoContratados= 5;
		
		Contrato contrato = this.service.consultarContratoPorNumeroContrato("1");
		
		
		Assert.assertNotNull(contrato);
		Assert.assertEquals(numeroContrato, contrato.getNumero());
		Assert.assertEquals(nomePaciente, contrato.getNomePaciente());
		Assert.assertEquals(numeroPlanoContratados, contrato.getPlanoContratado().size());
		
	}
	
	/* Teste que verifica a atribuicao de um valor de desconto no contrato */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/RegistroTestExportPlanilha.sql" })
	@Test
	public void atribuirDescontoMustPassAtribuindoDescontoAoContrato() throws Exception {
		final Double valorDesconto = 20.00;
		Contrato contrato =this.service.atribuirDesconto("1", 20.00);
		
		Assert.assertNotNull(contrato);
		Assert.assertEquals(valorDesconto, contrato.getDesconto());
		
	}
//	**************************************************** Must Fail *************************************************************************

/* Teste que consulta um contrato por uma biometria nao cadastrada*/
	
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
		"/dataset/PlanoContratado.sql","/dataset/RegistroTestExportPlanilha.sql","/dataset/Config.sql" })
	@Test(expected = IllegalArgumentException.class)
	@WithUserDetails("henrique_nitatori@hotmail.com")
	public void consultarContratoPelaBiometriaTestMustFailConsultandoContratosComBiometriaNaoCadastrada() throws Exception {
		Contrato contrato = this.service.consultarContratosPorBiometria();
		Assert.assertNotNull(contrato);
		Assert.assertEquals("Henrique Nitatori", contrato.getNomePaciente());
	}
	
	/* Teste que verifica o import da planilha de contratos sem os dias da semana*/
	
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
		"/dataset/Config.sql", "/dataset/PlanoContratado.sql" })
	@Test(expected = ImportPlanilhaException.class)
	@WithUserDetails("henrique_nitatori@hotmail.com")
	public void importarPlanilhaDeContratosTestMustFailVerificandoOImportDaPlanilhaSemOsDiasSemana() throws Exception {		
		String basePath = new File("").getAbsolutePath();
		FileInputStream fileInputStream = new FileInputStream(
				basePath + "/src/test/resources/PlanilhasDeTeste/importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletaDiasSemanaFaltando.xlsx");
		byte[] arquivoBytes = IOUtils.toByteArray(fileInputStream);
		this.service.importarPlanilhaContratos(new FileTransfer("importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletaDiasSemanaFaltando.xlsx", "xls", arquivoBytes));
	}

	/* Teste que verifica o import da planilha de contratos sem o nome do paciente*/
	
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
		"/dataset/Config.sql", "/dataset/PlanoContratado.sql" })
	@Test(expected = ValidationException.class)
	@WithUserDetails("henrique_nitatori@hotmail.com")
	public void importarPlanilhaDeContratosTestMustFailVerificandoOImportDaPlanilhaSemONomeDoPaciente() throws Exception {		
		String basePath = new File("").getAbsolutePath();
		FileInputStream fileInputStream = new FileInputStream(
				basePath + "/src/test/resources/PlanilhasDeTeste/importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletasFaltandoNomePacient.xlsx");
		byte[] arquivoBytes = IOUtils.toByteArray(fileInputStream);
		this.service.importarPlanilhaContratos(new FileTransfer("importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletasFaltandoNomePacient.xlsx", "xls", arquivoBytes));
	}
	
	/* Teste que verifica o import da planilha de contratos sem o numero de sessoes*/
	
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
		"/dataset/Config.sql", "/dataset/PlanoContratado.sql" })
	@Test(expected = ImportPlanilhaException.class)
	@WithUserDetails("henrique_nitatori@hotmail.com")
	public void importarPlanilhaDeContratosTestMustFailVerificandoOImportDaPlanilhaSemONumeroDeSessoes() throws Exception {		
		String basePath = new File("").getAbsolutePath();
		FileInputStream fileInputStream = new FileInputStream(
				basePath + "/src/test/resources/PlanilhasDeTeste/importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletasFaltandoNumeroDeSessoes.xlsx");
		byte[] arquivoBytes = IOUtils.toByteArray(fileInputStream);
		this.service.importarPlanilhaContratos(new FileTransfer("importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletasFaltandoNumeroDeSessoes.xlsx", "xls", arquivoBytes));
	}
	
	/* Teste que verifica o import da planilha de contratos sem a saida padrao*/
	
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
		"/dataset/Config.sql", "/dataset/PlanoContratado.sql" })
	@Test(expected = ImportPlanilhaException.class)
	@WithUserDetails("henrique_nitatori@hotmail.com")
	public void importarPlanilhaDeContratosTestMustFailVerificandoOImportDaPlanilhaSemASaidaPadra() throws Exception {		
		String basePath = new File("").getAbsolutePath();
		FileInputStream fileInputStream = new FileInputStream(
				basePath + "/src/test/resources/PlanilhasDeTeste/importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletasFaltandoSaidaPadrao.xlsx");
		byte[] arquivoBytes = IOUtils.toByteArray(fileInputStream);
		this.service.importarPlanilhaContratos(new FileTransfer("importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletasFaltandoSaidaPadrao.xlsx", "xls", arquivoBytes));
	}

	/* Teste que verifica o import da planilha de contratos sem o valor do plano*/
	
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
		"/dataset/Config.sql", "/dataset/PlanoContratado.sql" })
	@Test(expected = ImportPlanilhaException.class)
	@WithUserDetails("henrique_nitatori@hotmail.com")
	public void importarPlanilhaDeContratosTestMustFailVerificandoOImportDaPlanilhaSemOValorPlano() throws Exception {		
		String basePath = new File("").getAbsolutePath();
		FileInputStream fileInputStream = new FileInputStream(
				basePath + "/src/test/resources/PlanilhasDeTeste/importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletasFaltandoValorDoPlano.xlsx");
		byte[] arquivoBytes = IOUtils.toByteArray(fileInputStream);
		this.service.importarPlanilhaContratos(new FileTransfer("importPlanilhaContratosTestMustFailVerificandoErroDeImportacaoPorPlanilhaIncompletasFaltandoValorDoPlano.xlsx", "xls", arquivoBytes));
	}
	
	/* Teste que verifica a consulta de um contrato com numero contrato invalido*/
	
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql" })
	@Test(expected = IllegalArgumentException.class)
	@WithUserDetails("henrique_nitatori@hotmail.com")
	public void consultarContratoMustFailConsultandoContratoInexistente() throws Exception {
		this.service.consultarContratoPorNumeroContrato("20");
	}
	
	/* Teste que verifica a atribuicao de um valor de desconto em um contrato sem planos cadastrados */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/RegistroTestExportPlanilha.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void atribuirDescontoMustPassAtribuindoDescontoAoContratoSemPlanoCadastrado() throws Exception {
		this.service.atribuirDesconto("3", 20.00);
	}
	
	/* Teste que verifica a atribuicao de um valor de desconto em um contrato sem o valor do desconto informado */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/RegistroTestExportPlanilha.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void atribuirDescontoMustPassAtribuindoDescontoAoContratoSemOValorDoDescontoInformado() throws Exception {
		this.service.atribuirDesconto("3", null);

	}

}
