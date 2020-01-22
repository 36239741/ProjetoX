package com.br.projetox.test.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import com.br.projetox.entity.ConfiguracaoParametro;
import com.br.projetox.entity.Registro;
import com.br.projetox.entity.Situacao;
import com.br.projetox.exception.RegistroException;
import com.br.projetox.repository.ConfigParametrosRepository;
import com.br.projetox.repository.RegistroRepository;
import com.br.projetox.service.RegistroService;

import javassist.NotFoundException;


public class RegistroServiceTest extends AbstractIntegrationTest {
	@Autowired
	private RegistroService registroService;
	
	@Autowired
	private ConfigParametrosRepository configRepository;
	
	@Autowired
	private RegistroRepository registroRepository;
	
	/* SALVA UM HORARIO DE ENTRADA */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql" })
	@Test
	public void saveHorarioEntradaAtivoTestMustPassSalvaUmHorarioDeEntrada() throws NotFoundException {
		Registro registro =this.registroService.saveHorarioEntrada("2", "1");
		Assert.assertNotNull(registro);
	}
	
	/* SALVA UM HORARIO DE SAIDA */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void saveHorarioSaidaAtivoTestMustPassSalvaUmHorarioDeSaida() throws NotFoundException {
		Registro registro = this.registroService.saveHorarioSaida("1");
		Assert.assertNotNull(registro);

	}
	/* TESTA A TROCA DA SITUACAO DO CONTRATO PARA AUSENCIA DO PROFISSIONAL */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void exchangeOfContractStatusTestMustPassTestaATrocaDeSituacaoAusenciaProfissional() throws NotFoundException {
		final Double valorTotalPlano = 1000.00;
		Registro registro = this.registroService.exchangeOfContractStatus("AUSENCIA_DO_PROFISSIONAL", 3L, null,null);
		Assert.assertNotNull(registro);
		Assert.assertEquals(Situacao.AUSENCIA_DO_PROFISSIONAL, registro.getSituacao());
		Assert.assertEquals(valorTotalPlano, registro.getPlanoContratado().getValorTotal());

	}
	/* TESTA A BUSCA DE REGISTROS ATRAVES DE DATAS */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void findByDataMustPassBuscandoRegistroAtravesDeData() throws NotFoundException {
		Page<Registro> page = this.registroService.findByDate("2019-12-05","2019-12-06","1", 0, 1);
		Assert.assertNotNull(page.getContent());
		Assert.assertEquals(1, page.getTotalElements());

	}
	/* TESTA A TROCA DA SITUACAO DO CONTRATO PARA TROCA DE SERVICO */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void exchangeOfContractStatusTestMustPasstTestaATrocaDoServicoPorUmComMaiorValor() throws NotFoundException {
		final Double valorPlano = 45.00;
		final Double valorTotal = 2035.00;

		Registro registro = this.registroService.exchangeOfContractStatus("TROCA_DE_SERVICO", 4L, "Terapia Ocupacional", valorPlano);
		
		Assert.assertNotNull(registro);
		Assert.assertEquals(Situacao.TROCA_DE_SERVICO, registro.getSituacao());
		Assert.assertEquals(valorTotal, registro.getPlanoContratado().getValorTotal());

	}
	
	/* TESTA A TROCA DA SITUACAO DO CONTRATO PARA AUSENCIA DO PROFISSIONAL */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void exchangeOfContractStatusTestMustPasstTestaATrocaDeServicoPorUmServicoComValorMenor() throws NotFoundException {
		final Double valorPlano = 45.00;
		final Double valorTotal = 1045.00;
		Registro registro = this.registroService.exchangeOfContractStatus("TROCA_DE_SERVICO", 3L, "Terapia Ocupacional",valorPlano);
		
		Assert.assertNotNull(registro);
		Assert.assertEquals(Situacao.TROCA_DE_SERVICO, registro.getSituacao());
		Assert.assertEquals(valorTotal, registro.getPlanoContratado().getValorTotal());

	}

	
	/* TESTA A EXPORTACAO DE UMA PLANILHHA COM OS REGISTROS */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/RegistroTestExportPlanilha.sql","/dataset/Config.sql" })
	@Test
	public void exportPlanilhaRegistrosTestMustPassExportaUmaPlanilhaDeResigtros() throws NotFoundException, IOException {
		ByteArrayOutputStream bytePLanilha = this.registroService.createPlanilhaRegistros("1");
		FileOutputStream createPlanilha = new FileOutputStream("teste.xlsx");
		createPlanilha.write(bytePLanilha.toByteArray());
		createPlanilha.flush();
		createPlanilha.close();

	}
	
	
	/* SALVA UM HORARIO DE SAIDA E TESTA O VALOR DO ACRESCIMO DO VALOR TOTAL */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void saveHorarioSaidaTestMustPassTestaOAcrescimoNoValorTotal() throws NotFoundException {
		
		Registro registro = this.registroService.saveHorarioSaida("1");
		ConfiguracaoParametro configParametro = this.configRepository.findById(1L).get();
		Duration duration = Duration.between(registro.getPlanoContratado().getHorarioSaida(),  registro.getDataHoraSaida().toLocalTime());
		Double minutosAdicional = registro.getPlanoContratado().getValorPlano() + (duration.toMinutes() * configParametro.getValorMinutoAdicional());
		
		Assert.assertNotNull(registro);
		Assert.assertEquals(minutosAdicional, registro.getValorTotal());

	}
	
	/* SALVA UM HORARIO DE SAIDA E TESTA O VALOR DO ACRESCIMO DO VALOR TOTAL */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void saveHorarioSaidaTestMustPassSemAtraso() throws NotFoundException {
		
		Registro registro = this.registroService.saveHorarioSaida("1");

		
		Assert.assertNotNull(registro);
		Assert.assertEquals(registro.getPlanoContratado().getValorPlano(), registro.getValorTotal());

	}
	/* SALVA UM HORARIO DE SAIDA E TESTA O TOTAL DE HORAS */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void saveHorarioSaidaTestMustPassTestaOTotalDeHoras() throws NotFoundException {
		
		Registro registro = this.registroService.saveHorarioSaida("1");
		Duration duration = Duration.between(registro.getPlanoContratado().getHorarioEntrada(),  registro.getDataHoraSaida().toLocalTime());
		
		Assert.assertNotNull(registro);
		Assert.assertEquals(LocalTime.MIN.plusMinutes(duration.toMinutes()), registro.getTempoTotal());

	}
	
	/* BUSCA TODOS REGISTROS DE UM CONTRATO */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void findAllRegistroMustPassBuscaTodosRegistrosDeUmContrato() throws NotFoundException {
		final Integer numeroRegistro = 1;
		Page<Registro> registro = this.registroService.findAllRegistro("1", 0, 10);
		Assert.assertNotNull(registro.getContent());
		Assert.assertEquals(numeroRegistro, Integer.valueOf( registro.getContent().size()));

	}
	
	/* SALVA UM HORARIO DE SAIDA COM FALTA DO PACIENTE */
	@Sql({"/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
		"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/DiaConsulta.sql","/dataset/DiaConsulta-PlanoContratado.sql","/dataset/Config.sql" })
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Test
	public void recordTenMinutesAfterPlanTimeTestMustPassSalvaUmHorarioDeSaidaPreecheCampoDeEntradaDepoisDe10MinutosDaHoraDeEntrada() throws NotFoundException {
		this.registroService.recordTenMinutesAfterPlanTime();
	}
	
	/* MUST FAIL */
	
	/* SALVA UM HORARIO DE ENTRADA COM UM REGISTRO ABERTO */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql" })
	@Test(expected = RegistroException.class)
	public void saveHorarioEntradaAtivoTestMustFailSalvaUmHorarioDeEntradaComUmRegistroAberto() throws NotFoundException {
		this.registroService.saveHorarioEntrada("1", "1");
	}
	
	
	
	/* SALVA UM HORARIO DE SAIDA */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = RegistroException.class)
	public void saveHorarioEntradaAtivoTestMustFaillTentaSalvarUmHorarioDeSaidaSemHorarioEmAberto() throws NotFoundException {
		this.registroService.saveHorarioSaida("2");
	}
	
	/* TESTA A TROCA DA SITUACAO DO CONTRATO COM ID DO REGISTRO INVALIDO */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = RegistroException.class)
	public void exchangeOfContractStatusTestMustFailtTestaComIdRegistroInvalido() throws NotFoundException {
		final Double valorPlano = 45.00;
		Registro registro = this.registroService.exchangeOfContractStatus("TROCA_DE_SERVICO", 10L, "Terapia Ocupacional",45.0);
		
		Assert.assertNotNull(registro);
		Assert.assertEquals(Situacao.TROCA_DE_SERVICO, registro.getSituacao());
		Assert.assertEquals(valorPlano, registro.getPlanoContratado().getValorPlano());

	}
	/* TESTA A TROCA DE SITUACAO DO CONTRATO COM HORA DE SAIDA PREENCHIDA */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = RegistroException.class)
	public void exchangeOfContractStatusTestMustFailtTestaComHoraDeSaidaPreenchida() throws NotFoundException {
		final Double valorPlano = 45.00;
		Registro registro = this.registroService.exchangeOfContractStatus("TROCA_DE_SERVICO", 5L, "Terapia Ocupacional",45.0);
		
		Assert.assertNotNull(registro);
		Assert.assertEquals(Situacao.TROCA_DE_SERVICO, registro.getSituacao());
		Assert.assertEquals(valorPlano, registro.getPlanoContratado().getValorPlano());

	}
	
	/* TESTA A TROCA DE SITUACAO DO CONTRATO COM SITUACAO INVALIDA */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = RegistroException.class)
	public void exchangeOfContractStatusTestMustFailtTestaComSituacaoInvalida() throws NotFoundException {
		final Double valorPlano = 45.00;
		Registro registro = this.registroService.exchangeOfContractStatus("TROCA_DE_SERVICO", 6L, "Terapia Ocupacional",45.0);
		
		Assert.assertNotNull(registro);
		Assert.assertEquals(Situacao.TROCA_DE_SERVICO, registro.getSituacao());
		Assert.assertEquals(valorPlano, registro.getPlanoContratado().getValorPlano());

	}
	
	/* TESTA A TROCA DA SITUACAO DO CONTRATO COM O VALOR DA SESSAO NULO */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = RegistroException.class)
	public void exchangeOfContractStatusTestMustFailtTestaComValorDaSessaoNulo() throws NotFoundException {
		final Double valorPlano = 45.00;
		Registro registro = this.registroService.exchangeOfContractStatus("TROCA_DE_SERVICO", 3L, "Terapia Ocupacional",0.0);
		
		Assert.assertNotNull(registro);
		Assert.assertEquals(Situacao.TROCA_DE_SERVICO, registro.getSituacao());
		Assert.assertEquals(valorPlano, registro.getPlanoContratado().getValorPlano());

	}
	
	/* TESTA A BUSCA DE REGISTROS ATRAVES DE DATAS SEM O CAMPO DATA INICIAL */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = RegistroException.class)
	public void findByDataMustFaillBuscandoRegistroSemOCampoDeDataInicial() throws NotFoundException {
		Page<Registro> page = this.registroService.findByDate("","2019-12-06","1", 0, 1);
		Assert.assertNotNull(page.getContent());
		Assert.assertEquals(1, page.getTotalElements());

	}
	
	/* TESTA A BUSCA DE REGISTROS ATRAVES DE DATAS SEM O CAMPO DATA FINAL */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = RegistroException.class)
	public void findByDataMustFaillBuscandoRegistroSemOCampoDeDataFinal() throws NotFoundException {
		Page<Registro> page = this.registroService.findByDate("2019-12-04","","1", 0, 1);
		Assert.assertNotNull(page.getContent());
		Assert.assertEquals(1, page.getTotalElements());

	}
	
	/* TENTAR CRIAR UMA PLANILHA SEM REGISTROS NO CONTRATO */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/RegistroTestExportPlanilha.sql","/dataset/Config.sql" })
	@Test(expected = RegistroException.class)
	public void createUmaPlanilhaRegistrosRegistrosTestMustFailTentaCriarUmaPlanilhaSemRegistro() throws NotFoundException, IOException {
		
		ByteArrayOutputStream bytePLanilha = this.registroService.createPlanilhaRegistros("2");
		FileOutputStream createPlanilha = new FileOutputStream("teste.xlsx");
		createPlanilha.write(bytePLanilha.toByteArray());
		createPlanilha.flush();
		createPlanilha.close();

	}
	/* TESTA A BUSCA DE REGISTROS ATRAVES DE DATAS SEM O CAMPO CONTRATO ID */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = RegistroException.class)
	public void findByDataMustFaillBuscandoRegistroSemOCampoContratoId() throws NotFoundException {
		Page<Registro> page = this.registroService.findByDate("2019-12-04","2019-12-06","", 0, 1);
		Assert.assertNotNull(page.getContent());
		Assert.assertEquals(1, page.getTotalElements());

	}
	
	
	//************************ TESTES PARA SERVIÇO DE REGISTRAR SAÍDA AUTOMÁTICA *****************************//
	
	/* Teste que verifica um registro fechado automaticamente */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void registrarSaidaAutomaticaMustPass() {
		final Registro registro = this.registroRepository.findById(2L).orElse(null);
		this.registroService.registrarSaidaAutomatica(registro);
		Assert.assertNotNull(registro);
		Assert.assertNotNull(registro.getValorTotal());
		Assert.assertTrue(registro.getValorTotal().equals(1000.00));
		Assert.assertNotNull(registro.getDataHoraSaida());
		LocalDateTime dataHoraSaida = LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 10, 0));
		Assert.assertTrue(registro.getDataHoraSaida().equals(dataHoraSaida));

	}
	
	/* Teste que verifica um registro fechado automaticamente
	 * Falhar pois não foi passado registro */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void registrarSaidaAutomaticaMustFailSemPassarRegistro() {
		this.registroService.registrarSaidaAutomatica(null);

	}
	
	/* Teste que verifica um registro fechado automaticamente 
	 * Falhar pois a situação não é ATENDIMENTO NORMAL*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void registrarSaidaAutomaticaMustFailSituacaoErrada() {
		final Registro registro = this.registroRepository.findById(8L).orElse(null);
		this.registroService.registrarSaidaAutomatica(registro);

	}
	
	/* Teste que verifica um registro fechado automaticamente 
	 * Falhar pois registro já está fechado*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void registrarSaidaAutomaticaMustFailRegistroFechado() {
		final Registro registro = this.registroRepository.findById(9L).orElse(null);
		this.registroService.registrarSaidaAutomatica(registro);

	}
	
	//************************ TESTES PARA SERVIÇO DE REGISTRAR AUSÊNCIA DO PACIENTE AUTOMATICAMENTE *****************************//
	
	/* Teste que verifica um registro criado automaticamente devido à ausência do paciente
	 * Verificando dados gerais do registro */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void registrarAusenciaPacienteAutomaticamenteMustPass() throws NotFoundException {
		final Registro registro = this.registroService.registrarAusenciaPacienteAutomaticamente(5);
		
		Assert.assertNotNull(registro);
		
		Assert.assertNotNull(registro.getSituacao());
		Assert.assertTrue(registro.getSituacao().equals(Situacao.AUSENCIA_DO_PACIENTE));
		
		Assert.assertNotNull(registro.getDataHoraEntrada());
		LocalDateTime dataHoraEntrada = LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 45, 0));
		Assert.assertTrue(registro.getDataHoraEntrada().equals(dataHoraEntrada));
		
		Assert.assertNotNull(registro.getDataHoraSaida());
		LocalDateTime dataHoraSaida = LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 50, 0));
		Assert.assertTrue(registro.getDataHoraSaida().equals(dataHoraSaida));

	}
	
	/* Teste que verifica um registro criado automaticamente devido à ausência do paciente
	 * Verificando valor do registro, se tratando de um contrato do tipo PLANO */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void registrarAusenciaPacienteAutomaticamenteMustPassTipoContratoPlano() throws NotFoundException {
		final Registro registro = this.registroService.registrarAusenciaPacienteAutomaticamente(6);
		
		Assert.assertNotNull(registro);
		
		Assert.assertNotNull(registro.getValorTotal());
		Assert.assertTrue(registro.getValorTotal().equals(1000.00));

	}
	
	/* Teste que verifica um registro criado automaticamente devido à ausência do paciente
	 * Verificando valor do registro, se tratando de um contrato do tipo PARTICULAR */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void registrarAusenciaPacienteAutomaticamenteMustPassTipoContratoParticular() throws NotFoundException {
		final Registro registro = this.registroService.registrarAusenciaPacienteAutomaticamente(5);
		
		Assert.assertNotNull(registro);
		
		Assert.assertNotNull(registro.getValorTotal());
		Assert.assertTrue(registro.getValorTotal().equals(0.0));

	}
	
	
	/* Teste que verifica um registro criado automaticamente devido à ausência do paciente
	 * Falha pois há um registro aberto para o plano informado */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = RegistroException.class)
	public void registrarAusenciaPacienteAutomaticamenteMustFailHasRegistroAberto() throws NotFoundException {
		final Registro registro = this.registroService.registrarAusenciaPacienteAutomaticamente(7);

	}
	
	/* Teste que verifica um registro criado automaticamente devido à ausência do paciente
	 * Falha pois o plano não está ativo */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void registrarAusenciaPacienteAutomaticamenteMustFailPlanoInativo() throws NotFoundException {
		final Registro registro = this.registroService.registrarAusenciaPacienteAutomaticamente(8);

	}
}
