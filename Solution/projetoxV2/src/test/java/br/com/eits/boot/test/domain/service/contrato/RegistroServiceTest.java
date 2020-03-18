package br.com.eits.boot.test.domain.service.contrato;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import br.com.eits.boot.test.domain.AbstractIntegrationTests;
import br.com.genegouveia.domain.entity.contrato.Contrato;
import br.com.genegouveia.domain.entity.contrato.Registro;
import br.com.genegouveia.domain.entity.contrato.Situacao;
import br.com.genegouveia.domain.repository.contrato.ContratoRepository;
import br.com.genegouveia.domain.repository.contrato.RegistroRepository;
import br.com.genegouveia.domain.service.contrato.RegistroService;
import javassist.NotFoundException;


public class RegistroServiceTest extends AbstractIntegrationTests {
	@Autowired
	private RegistroService registroService;
	
	@Autowired
	private ContratoRepository contratoRepository;
	
	@Autowired
	private RegistroRepository registroRepository;
	
	/* Teste que registra a ausencia do profissional*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void registrarAusenciaDoProfisionalMustPass() throws IOException, NumberFormatException, NotFoundException {
		final Double valorTotalRegistro = 0.0;
		final Double valorTotalPlano = 1990.00;
		Registro registro = this.registroService.registrarAusenciaDoProfisional(5L);
		
		Assert.assertNotNull(registro);
		Assert.assertEquals(Situacao.AUSENCIA_DO_PROFISSIONAL, registro.getSituacao());
		Assert.assertEquals(valorTotalRegistro, registro.getValorTotal());
		Assert.assertEquals(valorTotalPlano, registro.getPlanoContratado().getValorTotal());
	}
	
	/* Teste que faz uma alteracao no servico de um registro*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void registrarAlteracaoServicoMustPass() throws IOException, NumberFormatException, NotFoundException {
		final Double valorSessao = 50.00;
		final Long registroId = 15L;
		final Double valorTotalRegistro = 150.00;
		final Situacao situacaoRegistro = Situacao.TROCA_DE_SERVICO;
		Registro registro = this.registroService.registrarAlteracaoServico(registroId, valorSessao);
		Assert.assertNotNull(registro);
		Assert.assertEquals(registroId, registro.getId());
		Assert.assertEquals(valorTotalRegistro, registro.getValorTotal());
		Assert.assertEquals(situacaoRegistro, registro.getSituacao());
			
	}
	
	/* Teste que salva um horario de saida de um paciente e verifica o tempo total da consulta*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void salvarHorarioSaidaMustPassTestandoOTempoTotalDaConsulta() throws IOException, NumberFormatException, NotFoundException {
		Registro ultimoRegistro = this.registroRepository.consultarUltimoRegistroContrato("1");
		
		Duration tempoTotal = Duration.between(ultimoRegistro.getDataHoraEntrada().toLocalTime(),
				LocalTime.now(ZoneId.of("America/Maceio")));
		
		Registro registro = this.registroService.salvarHorarioSaida("1");
		
		Assert.assertNotNull(registro);
		Assert.assertEquals(LocalTime.MIN.plusMinutes(tempoTotal.toMinutes()), registro.getTempoTotal());
			
			
	}
	
	/* Teste que salva um horario de saida de um paciente com valor de tolerancia incluso*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void salvarHorarioSaidaMustPassTestandoValorTolerancia() throws IOException, NumberFormatException, NotFoundException {
		Registro ultimoRegistro = this.registroRepository.consultarUltimoRegistroContrato("1");
		
		Duration verificarMinutosAdicionais = Duration.between(
				LocalTime.now(ZoneId.of("America/Maceio")),
				ultimoRegistro.getPlanoContratado().getHorarioSaida().plusMinutes(5));
		
		final Double valorUltrapassado = (verificarMinutosAdicionais.toMinutes() > 0) ? 
				((verificarMinutosAdicionais.toMinutes() + 5) * 2.30) + ultimoRegistro.getPlanoContratado().getValorAtendimento()  : 
					ultimoRegistro.getPlanoContratado().getValorAtendimento(); 
			
			Registro registro = this.registroService.salvarHorarioSaida("1");
			Assert.assertNotNull(registro);
			Assert.assertEquals(valorUltrapassado, registro.getValorTotal());
	}
	
	/* Teste que salva um horario de saida de um paciente*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void salvarHorarioSaidaMustPass() throws IOException, NumberFormatException, NotFoundException {
			Registro registro = this.registroService.salvarHorarioSaida("1");
			Assert.assertNotNull(registro);
			Assert.assertEquals(LocalDateTime.now(ZoneId.of("America/Maceio")).toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm")) , registro.getDataHoraSaida().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm")));
			Assert.assertEquals(LocalDateTime.now(ZoneId.of("America/Maceio")).toLocalDate() , registro.getDataHoraSaida().toLocalDate());
	}
	
	/* Teste que verifica a consulta dos registro mensais de um plano contratado*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void consultarRegistroMensalMustPassConsultandoRegistroMensal() throws IOException, NumberFormatException, NotFoundException {
			
			LocalDate data = LocalDate.now();
			LocalDate dataInicial = LocalDate.of(data.getYear(),data.getMonthValue(),1);
			LocalDate ultimoDiaMes = dataInicial.withDayOfMonth(dataInicial.lengthOfMonth());
			LocalDate dataFinal = LocalDate.of(data.getYear(), data.getMonthValue(),ultimoDiaMes.getDayOfMonth());
			
			Page<Registro> registros = this.registroService.consultarRegistroMensal(2L, dataInicial.toString(), dataFinal.toString(), 10L, 0, 32);
			
			Assert.assertNotNull(registros.getContent());
			Assert.assertEquals(2, registros.getContent().size());
	}
	
	/* Teste que verifica o registro do horario de entrada do paciente*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void salvarHorarioEntradaMustPassRegistrandoHorarioEntrada() throws IOException, NumberFormatException, NotFoundException {
			final LocalTime horarioEntrada = LocalTime.of(LocalDateTime.now(ZoneId.of("America/Maceio")).getHour(), LocalDateTime.now(ZoneId.of("America/Maceio")).getMinute());
			final Situacao situacaoAtendimento = Situacao.ATENDIMENTO_NORMAL;
			final Double valorAtendimento = 50.00;
			Registro registro = this.registroService.salvarHorarioEntrada("2", 10L);
			Assert.assertNotNull(registro);
			Contrato contrato = this.contratoRepository.findById(2L).get();
			
			final LocalTime horarioEntradaRegistro =  LocalTime.of(LocalDateTime.now(ZoneId.of("America/Maceio")).getHour(), LocalDateTime.now(ZoneId.of("America/Maceio")).getMinute());;
			
			Assert.assertEquals(valorAtendimento, registro.getValorTotal());
			Assert.assertEquals(situacaoAtendimento, registro.getSituacao());
			Assert.assertEquals(contrato.getNumero(), registro.getPlanoContratado().getContrato().getNumero());
			Assert.assertEquals(contrato.getNomePaciente(), registro.getPlanoContratado().getContrato().getNomePaciente());
			Assert.assertEquals(horarioEntrada,horarioEntradaRegistro );
	}
	
	/* Teste que verifica a exportacao de registro de entrada e saida de um contrato*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void exportarRegistrosEntradaSaidaMustPassExportandoRegistroEntradaSaidaDeUmContrato() throws IOException {
		ByteArrayOutputStream bytePLanilha = this.registroService.exportarRegistrosEntradaSaida("1");
		File file = new File("teste.xlsx");
		FileOutputStream createPlanilha = new FileOutputStream(file);
		createPlanilha.write(bytePLanilha.toByteArray());
		createPlanilha.flush();
		createPlanilha.close();
	}
	
	
	/* Teste que verifica a busca de todos os contratos de um registro */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void consultarRegistrosMustPassBuscandoTodosRegistrosDeUmContrato() {
		final int totalRegistros = 15;
		Page<Registro> registros = this.registroService.consultarTodosRegistrosDoContrato("1", 0, 10);
		
		Assert.assertNotNull(registros);
		Assert.assertEquals(totalRegistros, registros.getTotalElements());

	}

	//************************ TESTES PARA SERVIÇO QUE RECUPERA OS REGISTROS ABERTOS *****************************//
	
	
	/* Teste que verifica um registro fechado automaticamente */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void listRegistrosAbertosMustPass() {
		final List<Registro> registrosAbertos = this.registroService.listRegistrosAbertos();
		Assert.assertNotNull(registrosAbertos);
		Assert.assertEquals(7, registrosAbertos.size());

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
	
	//***************************************************** Must Fail ****************************************************************************************
	
	/* Teste que verifica o registro de ausencia do profissional com o horario de saida preenchido*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void registrarAusenciaDoProfisionalMustFailRegistroAusenciaComOHorarioDeSaidaPreenchido() throws IOException, NumberFormatException, NotFoundException {
			this.registroService.salvarHorarioSaida("5");
			
	}
	
	/* Teste que verifica o registro de ausencia do profissional com a situacao do atendimento diferente de ausencia do paciente*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void registrarAusenciaDoProfisionalMustFailRegistroAusenciaComASituacaoDeAtendimentoDieferenteDeAusenciaPaciente() throws IOException, NumberFormatException, NotFoundException {
			this.registroService.salvarHorarioSaida("4");
			
	}
	
	/* Teste que verifica a alteracao do servico com horario de saida preechido*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void registrarAlteracaoServicoMustFailRegistrandoTrocaServicoComRegistroComHorarioDeSaidaPreechido() throws IOException, NumberFormatException, NotFoundException {
			this.registroService.salvarHorarioSaida("9");
			
	}
	
	/* Teste que verifica a alteracao do servico com registro id invalido*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void registrarAlteracaoServicoMustFailRegistrandoTrocaServicoComRegistroEmSituacaoDiferenteDeAtendimentoNormal() throws IOException, NumberFormatException, NotFoundException {
			this.registroService.salvarHorarioSaida("8");
			
	}
	
	/* Teste que verifica a alteracao do servico com registro id invalido*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void registrarAlteracaoServicoMustFailRegistrandoTrocaServicoComIdRegistroInvalido() throws IOException, NumberFormatException, NotFoundException {
			this.registroService.salvarHorarioSaida("20");
			
	}
	
	
	/* Teste que verifica o registro do horario de saida do paciente sem existir nenhum registro em aberto*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void salvarHorarioSaidaMustFailRegistrandoHorarioSaidaSemNenhumRegistroEmAberto() throws IOException, NumberFormatException, NotFoundException {
			this.registroService.salvarHorarioSaida("2");
			
	}
	
	/* Teste que verifica o registro do horario de saida do paciente sem existir nenhum registro cadastrado*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void salvarHorarioSaidaMustFailRegistrandoHorarioSaidaSemNenhumRegistroCadastrado() throws IOException, NumberFormatException, NotFoundException {
			this.registroService.salvarHorarioSaida("3");
			
	}
	
	/* Teste que verifica o registro do horario de entrada do paciente contendo um registro em aberto*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void salvarHorarioEntradaMustFailRegistrandoHorarioEntradaContendoUmRegistroEmAberto() throws IOException, NumberFormatException, NotFoundException {
			this.registroService.salvarHorarioEntrada("1", 8L);
			
	}
	
	/* Teste que verifica a exportacao de  registro de entrada e saida de um contrato sem registro */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void exportarRegistrosEntradaSaidaMustFailExportandoRegistroEntradaSaidaDeUmContratoSemRegistro() throws IOException {
		this.registroService.exportarRegistrosEntradaSaida("3");

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
		final Registro registro = this.registroService.registrarAusenciaPacienteAutomaticamente(10);
		
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
		final Registro registro = this.registroService.registrarAusenciaPacienteAutomaticamente(10);
		
		Assert.assertNotNull(registro);
		
		Assert.assertNotNull(registro.getValorTotal());
		Assert.assertTrue(registro.getValorTotal().equals(0.0));

	}
	
	/* Teste que verifica um registro criado automaticamente devido à ausência do paciente
	 * Verificando valor do registro, se tratando de um contrato do tipo PARTICULAR */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void registrarAusenciaPacienteAutomaticamenteMustPassTipoContratoParticular() throws NotFoundException {
		final Registro registro = this.registroService.registrarAusenciaPacienteAutomaticamente(10);
		
		Assert.assertNotNull(registro);
		
		Assert.assertNotNull(registro.getValorTotal());
		Assert.assertTrue(registro.getValorTotal().equals(0.0));

	}
	
	
	/* Teste que verifica um registro criado automaticamente devido à ausência do paciente
	 * Falha pois há um registro aberto para o plano informado */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void registrarAusenciaPacienteAutomaticamenteMustFailHasRegistroAberto() throws NotFoundException {
		final Registro registro = this.registroService.registrarAusenciaPacienteAutomaticamente(2);

	}
	
	/* Teste que verifica um registro criado automaticamente devido à ausência do paciente
	 * Falha pois o plano não está ativo */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void registrarAusenciaPacienteAutomaticamenteMustFailPlanoInativo() throws NotFoundException {
		final Registro registro = this.registroService.registrarAusenciaPacienteAutomaticamente(9);

	}
	
	
}
