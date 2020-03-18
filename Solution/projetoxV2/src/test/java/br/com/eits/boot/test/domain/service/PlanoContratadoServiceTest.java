package br.com.eits.boot.test.domain.service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import br.com.eits.boot.test.domain.AbstractIntegrationTests;
import br.com.genegouveia.domain.entity.contrato.Contrato;
import br.com.genegouveia.domain.entity.contrato.DiaConsulta;
import br.com.genegouveia.domain.entity.contrato.DiasSemana;
import br.com.genegouveia.domain.entity.contrato.PlanoContratado;
import br.com.genegouveia.domain.entity.contrato.Servico;
import br.com.genegouveia.domain.entity.contrato.TipoContrato;
import br.com.genegouveia.domain.repository.ConfigParametrosRepository;
import br.com.genegouveia.domain.repository.ContratoRepository;
import br.com.genegouveia.domain.repository.PlanoContratoRepository;
import br.com.genegouveia.domain.repository.ServicoRepository;
import br.com.genegouveia.domain.service.PlanoContratadoService;

public class PlanoContratadoServiceTest extends AbstractIntegrationTests {
	@Autowired
	private PlanoContratadoService planoContratoService;
	
	@Autowired
	private PlanoContratoRepository planoContratadoRepository;

	@Autowired
	private ContratoRepository contratoRepository;

	@Autowired
	private ServicoRepository servicoRepository;
	
	@Autowired
	private ConfigParametrosRepository configRepository;
	
	/* Teste que verifica o retorno do saldo mensal de um plano contratado */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/Registro.sql", "/dataset/DiaConsulta.sql",
			"/dataset/DiaConsulta-PlanoContratado.sql"})
	@Test
	public void consultarPlanosAtivosContratoMustPassVerificandoRetornoSaldoMensalPlanoContratado() throws Exception {
		final Double saldoMensalPLanoContratado = 1500.00;
		
		List<PlanoContratado> planosContratados = this.planoContratoService.consultarPlanosDisponiveisDoContrato("2");
		
		Assert.assertNotNull(planosContratados);
		Assert.assertEquals(saldoMensalPLanoContratado, planosContratados.get(0).getSaldoMensal());
	}
	
	/* Teste que verifica o retorno ordenado dos dias semana de uma consulta de planos de um contrato */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/RegistroTestExportPlanilha.sql", "/dataset/DiaConsulta.sql",
			"/dataset/DiaConsulta-PlanoContratado.sql"})
	@Test
	public void consultarPlanosAtivosContratoMustPassVerificandoRetornoOrdenadoDiasSemana() throws Exception {
		final DiasSemana segunda = DiasSemana.QUINTA;
		final DiasSemana terca = DiasSemana.SEXTA;
		
		List<PlanoContratado> planosContratados = this.planoContratoService.consultarPlanosDisponiveisDoContrato("1");
		
		Assert.assertNotNull(planosContratados);
		Assert.assertEquals(segunda, planosContratados.get(0).getDiaConsulta().get(0).getDiasSemana());
		Assert.assertEquals(terca, planosContratados.get(0).getDiaConsulta().get(1).getDiasSemana());
	}
	
	/* Teste que verifica a consulta de todos planos ativos do contrato */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/RegistroTestExportPlanilha.sql" })
	@Test
	public void consultarPlanosAtivosContratoMustPassVerificandoAConsultaDosPlanosAtivosPorContrato() throws Exception {
		final int numeroDePlanosAtivos = 4;
		
		List<PlanoContratado> planosContratados = this.planoContratoService.consultarPlanosDisponiveisDoContrato("1");
		Assert.assertNotNull(planosContratados);
		Assert.assertEquals(numeroDePlanosAtivos, planosContratados.size());
	}
	
	/* Teste que verifica a soma dos minutos totais contratado no servico psicologia*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/RegistroTestExportPlanilha.sql" })
	@Test
	public void visualizarValoresCompiladosDosPlanosMustPassVerificandoOTotalDeMinutosDoPsicologia() throws Exception {
		
		final Double somaTodosPlanosTipoPlano = 0 *  (double) this.configRepository.findById(1L).get().getTempoSessao().getMinute();
		HashMap<String, Double> map = this.planoContratoService.visualizarValoresCompiladosDosPlanos();
		
		Assert.assertNotNull(map);
		Assert.assertEquals(somaTodosPlanosTipoPlano, map.get("Psicologia"));
	}
	
	/* Teste que verifica a soma dos minutos totais contratado no servico neuropsicopedagogia*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/RegistroTestExportPlanilha.sql" })
	@Test
	public void visualizarValoresCompiladosDosPlanosMustPassVerificandoOTotalDeMinutosDoPlanoNeuropsicopedagogia() throws Exception {
		
		final Double somaTodosPlanosTipoPlano = 9.00 *  (double) this.configRepository.findById(1L).get().getTempoSessao().getMinute();
		HashMap<String, Double> map = this.planoContratoService.visualizarValoresCompiladosDosPlanos();
		
		Assert.assertNotNull(map);
		Assert.assertEquals(somaTodosPlanosTipoPlano, map.get("Neuropsicopedagogia"));
	}
	
	/* Teste que verifica a soma de todos valor total planos Contratados ativos do tipo plano */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/RegistroTestExportPlanilha.sql" })
	@Test
	public void visualizarValoresCompiladosDosPlanosMustPassVerificandoOValotTotalPlanoContratoTipoPlano() throws Exception {
		final Double somaTodosPlanosTipoPlano = 4000.00;
		HashMap<String, Double> map = this.planoContratoService.visualizarValoresCompiladosDosPlanos();
		
		Assert.assertNotNull(map);
		Assert.assertEquals(somaTodosPlanosTipoPlano, map.get("plano"));
	}
	
	/* Teste que verifica a soma de todos valor total planos Contratados ativos do tipo Particular */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/RegistroTestExportPlanilha.sql" })
	@Test
	public void visualizarValoresCompiladosDosPlanosMustPassVerificandoOValotTotalPlanoContratoTipoParticular() throws Exception {
		final Double somaTodosPlanosTipoPlano = 6000.00;
		HashMap<String, Double> map = this.planoContratoService.visualizarValoresCompiladosDosPlanos();
		
		Assert.assertNotNull(map);
		Assert.assertEquals(somaTodosPlanosTipoPlano, map.get("particular"));
	}
	
	/* Teste que verifica a soma de todos valor total dos planos Contratados ativos */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/RegistroTestExportPlanilha.sql" })
	@Test
	public void visualizarValoresCompiladosDosPlanosMustPassVerificandoOValotTotalPlanoContratosAtivos() throws Exception {
		final Double somaTodosPlanosTipoPlano = 10000.00;
		HashMap<String, Double> map = this.planoContratoService.visualizarValoresCompiladosDosPlanos();
		
		Assert.assertNotNull(map);
		Assert.assertEquals(somaTodosPlanosTipoPlano, map.get("total"));
	}
	
	/* Teste que salva um plano contratado */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/RegistroTestExportPlanilha.sql" })
	@Test
	public void salvarPlanoContratadoMustPassSalvandoUmPlanoContratado() throws Exception {
		Contrato contrato = this.contratoRepository.findByNumero("3").get();
		PlanoContratado plano  = new PlanoContratado();
		DiaConsulta diaConsulta = new DiaConsulta();
		diaConsulta.setDiasSemana(DiasSemana.SEGUNDA);
		plano.getDiaConsulta().add(diaConsulta);
		LocalTime horarioEntrada = LocalTime.parse("09:20:00");
		plano.setHorarioEntrada(horarioEntrada);
		LocalTime horarioSaida = LocalTime.parse("10:00:00");
		plano.setHorarioSaida(horarioSaida);
		Servico servico = this.servicoRepository.findByServicoIgnoreCase("Psicologia");
		plano.setServico(servico);
		plano.setSessao(4);
		plano.setTipoContrato(TipoContrato.PARTICULAR);
		plano.setValorTotal(100.00);
		plano.setValorAtendimento(100.00);
		plano.setValorSessao(25.00);
		plano.setContrato(contrato);
		PlanoContratado savePlanoContratado = this.planoContratoService.salvarPlanoContratado(plano);
		
		Assert.assertNotNull(savePlanoContratado);
	}
	
	/* Teste que atualiza um plano contratado */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/RegistroTestExportPlanilha.sql" })
	@Test
	public void atualizarPlanoContratadoMustPassAtualizandoUmPlanoContratado() throws Exception {
		final Double valorTotal = 900.00;
		PlanoContratado planoContratado = this.planoContratadoRepository.findById(1L).get();
		
		planoContratado.setHorarioEntrada(LocalTime.parse("17:00:00"));
		planoContratado.setHorarioSaida(LocalTime.parse("17:50:00"));
		Servico servico = this.servicoRepository.findByServicoIgnoreCase("Psicologia");
		planoContratado.setServico(servico);
		planoContratado.setSessao(2);
		planoContratado.setTipoContrato(TipoContrato.PLANO);
		planoContratado.setValorTotal(900.00);
		
		PlanoContratado atualizandoPlanoContratado = this.planoContratoService.salvarPlanoContratado(planoContratado);
		
		Assert.assertNotNull(atualizandoPlanoContratado);
		
		Assert.assertEquals(LocalTime.parse("17:00:00"), atualizandoPlanoContratado.getHorarioEntrada());
		Assert.assertEquals(LocalTime.parse("17:50:00"), atualizandoPlanoContratado.getHorarioSaida());
		Assert.assertEquals(2, atualizandoPlanoContratado.getSessao());
		Assert.assertEquals(TipoContrato.PLANO, atualizandoPlanoContratado.getTipoContrato());
		Assert.assertEquals(valorTotal, atualizandoPlanoContratado.getValorTotal());
	}
	
	/* Teste que deleta um plano contratado */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/RegistroTestExportPlanilha.sql" })
	@Test
	public void deletePlanoContratadoMustPassDeletandoUmPlanoContratado() throws Exception {
		this.planoContratoService.removerPlanoContratado(1L);
		PlanoContratado plano = this.planoContratadoRepository.findById(1L).get();
		
		Assert.assertEquals(false, plano.getAtivo());
	}
	
	
// ******************************************************* Must Fail ******************************************************************
	
	/* Teste que salva um plano contratado duplicado */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/RegistroTestExportPlanilha.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void salvarPlanoContratadoMustFailSalvandoUmPlanoContratadoDuplicado() throws Exception {
		Contrato contrato = this.contratoRepository.findByNumero("1").get();
		PlanoContratado plano  = new PlanoContratado();
		DiaConsulta diaConsulta = new DiaConsulta();
		diaConsulta.setDiasSemana(DiasSemana.SEGUNDA);
		plano.getDiaConsulta().add(diaConsulta);
		LocalTime horarioEntrada = LocalTime.parse("09:20:00");
		plano.setHorarioEntrada(horarioEntrada);
		LocalTime horarioSaida = LocalTime.parse("10:00:00");
		plano.setHorarioSaida(horarioSaida);
		Servico servico = this.servicoRepository.findById(4L).get();
		plano.setServico(servico);
		plano.setSessao(4);
		plano.setTipoContrato(TipoContrato.PARTICULAR);
		plano.setValorTotal(100.00);
		plano.setValorAtendimento(100.00);
		plano.setValorSessao(25.00);
		plano.setContrato(contrato);
		this.planoContratoService.salvarPlanoContratado(plano);
	}
	
	/* Teste que deleta um plano contratado nao cadastrado*/
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Config.sql","/dataset/RegistroTestExportPlanilha.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void deletePlanoContratadoMustFailDeletandoUmPlanoContratadoNaoCadastrado() throws Exception {
		this.planoContratoService.removerPlanoContratado(20L);

	}
	

}