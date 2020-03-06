package com.br.projetox.test.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import com.br.projetox.entity.Contrato;
import com.br.projetox.entity.DiaConsulta;
import com.br.projetox.entity.DiasSemana;
import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.Servico;
import com.br.projetox.entity.TipoContrato;
import com.br.projetox.exception.DuplicatePlanoContratadoException;
import com.br.projetox.exception.FingerPrintException;
import com.br.projetox.exception.MapPlanoContratadoException;
import com.br.projetox.repository.ContratoRepository;
import com.br.projetox.repository.PlanoContratoRepository;
import com.br.projetox.repository.ServicoRepository;
import com.br.projetox.service.PlanoContratadoService;

import javassist.NotFoundException;

public class PlanoContratadoServiceTest extends AbstractIntegrationTest {
	@Autowired
	private PlanoContratadoService planoContratoService;
	
	@Autowired
	private PlanoContratoRepository planoContratadoRepository;

	@Autowired
	private ContratoRepository contratoRepository;

	@Autowired
	private ServicoRepository servicoRepository;

	/* VERIFICA A BUSCA DE UM PLANO CONTRATADO */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql" })
	@Test
	public void consultarPlanoContratadoAtivoPorServiceIdContratoIdTipoContratoTestMustPassConsultandoUmPlanoContratado() throws NotFoundException {
		final Long servicoId = this.servicoRepository.findById(4L).orElse(null).getId();
		final Long contratoId = this.contratoRepository.findById(1L).orElse(null).getId();

		final PlanoContratado plano = this.planoContratoService.consultarPlanoContratadoAtivoPorServiceIdContratoIdTipoContrato(servicoId, contratoId,
				TipoContrato.PLANO);

		Assert.assertNotNull(plano);
		Assert.assertNotNull(plano.getId());
	}

	/*
	 * DELETA UM PLANO CONTRATADO
	 */
	 @WithUserDetails("henrique_nitatori@hotmail.com")
	  
	  @Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql",
	  "/dataset/Servico.sql", "/dataset/Contrato.sql",
	  "/dataset/PlanoContratado.sql" })
	  
	  @Test public void deleteLogicoTestMustPassDeletandoUmPlanoContratado()
	  throws NotFoundException { 
		 final Double valorTotalContrato = -1000.00;
		 this.planoContratoService.deleteLogico("1");
		 PlanoContratado plano = this.planoContratadoRepository.findById(Long.parseLong("1")).get();
		 Optional<Contrato> contrato = this.contratoRepository.findByNumero("1");
		 Assert.assertEquals(false, plano.getAtivo());
		 Assert.assertEquals(valorTotalContrato, contrato.get().getValorTotal());
		 }
	 
		/*
		 * BUSCA TODOS OS PLANOS DE UM DIA DA SEMANA
		 */
		 @WithUserDetails("henrique_nitatori@hotmail.com")
		  @Profile("dev")
		  @Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql",
		  "/dataset/Servico.sql", "/dataset/Contrato.sql",
		  "/dataset/PlanoContratado.sql",
		  "/dataset/DiaConsulta.sql",
		  "/dataset/DiaConsulta-PlanoContratado.sql"})
		  
		  @Test public void consultarContratoPorDiasSemanaTestMustPassBuscaTodosPlanosPeloDiaDaSemana()
		  { 
			 DiasSemana returnDiasSemana = null;
			 List<PlanoContratado> plano = this.planoContratoService.consultarContratoPorDiasSemana(DiasSemana.QUINTA);
			 for(PlanoContratado planos : plano) {
				 returnDiasSemana = planos.getDiaConsulta().get(0).getDiasSemana();
			 }
			 Assert.assertNotNull(plano);
			 Assert.assertEquals(DiasSemana.QUINTA, returnDiasSemana);
		  }
	 

	/*
	Verifica o retorno da funcao consultarSomaTotalPlanosAtivos verificando o valor total de plano tipo plano e particular
	e a soma dos dois
	 */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql" })
	@Test
	public void consultarSomaTotalPlanosAtivosMustPassVerificandoRetornoDaFuncao() throws NotFoundException {
		final Double particular = 8000.00;
		final Double plano = 6000.00;
		final Double total = 14000.00;
		Map<String, Double> map = this.planoContratoService.consultarSomaTotalPlanosAtivos();

		Assert.assertNotNull(map);
		Assert.assertEquals(plano,map.get("particular"));
		Assert.assertEquals(particular,map.get("plano"));
		Assert.assertEquals(total ,map.get("total"));

	}

	/* TESTA O METODO QUE MAPEIA UM OBJETO PLANO CONTRATADO ATRAVES DE UM HASHMAP */
	@Test
	public void mapPlanoContratandoMustPassMapeandoUmMapEmUmPlanoContratado() throws NotFoundException {
		Map<String, Object> map = new HashMap<>();
		map.put("horarioEntrada", "12:00");
		map.put("horarioSaida", "13:00");
		map.put("valorPlano", "120.00");
		List<String> lista = new ArrayList();
		lista.add("SEG");
		lista.add("TER");
		map.put("diaConsulta", lista);
		map.put("numeroContrato", "1");
		map.put("sessao", "2");
		map.put("tipoContrato", "plano");
		map.put("servico", "Psicologia");
		map.put("valorTotal", "1000");
		List<DiaConsulta> dias = new ArrayList<>();
		DiaConsulta dia1 = new DiaConsulta();
		dia1.setDiasSemana(DiasSemana.SEGUNDA);
		DiaConsulta dia2 = new DiaConsulta();
		dia2.setDiasSemana(DiasSemana.TERCA);
		dias.add(dia1);
		dias.add(dia2);
		Servico servico = this.servicoRepository.findByServicoIgnoreCase(map.get("servico").toString());
		PlanoContratado planoContratado = this.planoContratoService.mapearPlanoContratado(map);
		Assert.assertNotNull(planoContratado);
		Assert.assertEquals(dias, planoContratado.getDiaConsulta());
		Assert.assertEquals(LocalTime.parse(map.get("horarioEntrada").toString()), planoContratado.getHorarioEntrada());
		Assert.assertEquals(LocalTime.parse(map.get("horarioSaida").toString()), planoContratado.getHorarioSaida());
		Assert.assertEquals(Integer.parseInt(map.get("sessao").toString()), planoContratado.getSessao());
		Assert.assertEquals(map.get("numeroContrato"), "1");
		Assert.assertEquals(TipoContrato.PLANO, planoContratado.getTipoContrato());
		Assert.assertEquals(servico, planoContratado.getServico());

	}

	/* TESTA O METODO SAVE DO PLANO CONTRATADO */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql" })
	@Test
	public void mapPlanoContratandoMustPassSave() throws NotFoundException {
		Map<String, Object> map = new HashMap<>();
		map.put("horarioEntrada", "12:00");
		map.put("horarioSaida", "13:00");
		map.put("valorPlano", "120.00");
		List<String> lista = new ArrayList();
		lista.add("SEG");
		lista.add("TER");
		map.put("diaConsulta", lista);
		map.put("numeroContrato", "1");
		map.put("sessao", "1");
		map.put("tipoContrato", "plano");
		map.put("servico", "Psicologia");
		map.put("valorTotal", "1000");
		List<DiaConsulta> dias = new ArrayList<>();
		DiaConsulta dia1 = new DiaConsulta();
		dia1.setDiasSemana(DiasSemana.SEGUNDA);
		DiaConsulta dia2 = new DiaConsulta();
		dia2.setDiasSemana(DiasSemana.TERCA);
		dias.add(dia1);
		dias.add(dia2);
		this.planoContratoService.salvarPlanoContratado(map);
		Contrato contrato = this.contratoRepository.findByNumero("1").get();
		final Double valorTotalContrato = 2000.00;
		Assert.assertEquals(valorTotalContrato, contrato.getValorTotal());
	}

	/* TESTA O METODO UPDATE DO PLANO CONTRATADO PELO CAMPO HORARIO DE ENTRADA */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql" })
	@Test()
	public void mapPlanoContratandoMustPassUpdateAtualizandoOHorarioEntrada() throws NotFoundException {
		Map<String, Object> map = new HashMap<>();
		map.put("id", "1");
		map.put("horarioEntrada", "11:00");
		map.put("horarioSaida", "13:00");
		map.put("valorPlano", "120.00");
		List<String> lista = new ArrayList();
		lista.add("SEG");
		lista.add("TER");
		map.put("diaConsulta", lista);
		map.put("numeroContrato", "1");
		map.put("tipoContrato", "plano");
		map.put("sessao", "2");
		map.put("servico", "Psicologia");
		map.put("valorTotal", "1000");
		List<DiaConsulta> dias = new ArrayList<>();
		DiaConsulta dia1 = new DiaConsulta();
		dia1.setDiasSemana(DiasSemana.SEGUNDA);
		DiaConsulta dia2 = new DiaConsulta();
		dia2.setDiasSemana(DiasSemana.TERCA);
		dias.add(dia1);
		dias.add(dia2);
		this.planoContratoService.atualizarPlanoContratado(map);
		Servico servico = this.servicoRepository.findByServicoIgnoreCase(map.get("servico").toString());
		PlanoContratado findPlanoContratado = this.planoContratoService.consultarPlanoContratadoAtivoPorServiceIdContratoIdTipoContrato(servico.getId(),
				Long.parseLong(map.get("numeroContrato").toString()), TipoContrato.PLANO);
		Assert.assertNotNull(findPlanoContratado);
		Assert.assertEquals(LocalTime.parse(map.get("horarioEntrada").toString()),
				findPlanoContratado.getHorarioEntrada());
	}


	/* TESTA O METODO UPDATE DO PLANO CONTRATADO PELO CAMPO HORARIO DE SAIDA */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql" })
	@Test()
	public void mapPlanoContratandoMustPassUpdateAtualizandoOHorarioSaida() throws NotFoundException {
		Map<String, Object> map = new HashMap<>();
		map.put("id", "1");
		map.put("horarioEntrada", "11:00");
		map.put("horarioSaida", "14:00");
		map.put("valorPlano", "120.00");
		List<String> lista = new ArrayList();
		lista.add("SEG");
		lista.add("TER");
		map.put("diaConsulta", lista);
		map.put("sessao", "2");
		map.put("numeroContrato", "1");
		map.put("tipoContrato", "plano");
		map.put("servico", "Psicologia");
		map.put("valorTotal", "1000");
		List<DiaConsulta> dias = new ArrayList<>();
		DiaConsulta dia1 = new DiaConsulta();
		dia1.setDiasSemana(DiasSemana.SEGUNDA);
		DiaConsulta dia2 = new DiaConsulta();
		dia2.setDiasSemana(DiasSemana.TERCA);
		dias.add(dia1);
		dias.add(dia2);
		this.planoContratoService.atualizarPlanoContratado(map);
		Servico servico = this.servicoRepository.findByServicoIgnoreCase(map.get("servico").toString());
		PlanoContratado findPlanoContratado = this.planoContratoService.consultarPlanoContratadoAtivoPorServiceIdContratoIdTipoContrato(servico.getId(),
				Long.parseLong(map.get("numeroContrato").toString()), TipoContrato.PLANO);
		Assert.assertNotNull(findPlanoContratado);
		Assert.assertEquals(LocalTime.parse(map.get("horarioSaida").toString()), findPlanoContratado.getHorarioSaida());
	}



	/* TESTA O METODO UPDATE DO PLANO CONTRATADO PELO CAMPO TIPO CONTRATO */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql" })
	@Test()
	public void mapPlanoContratandoMustPassUpdateAtualizandoTipoContrato() throws NotFoundException {
		Map<String, Object> map = new HashMap<>();
		map.put("id", "1");
		map.put("horarioEntrada", "11:00");
		map.put("horarioSaida", "14:00");
		map.put("valorPlano", "150.00");
		List<String> lista = new ArrayList();
		lista.add("SEG");
		lista.add("TER");
		map.put("diaConsulta", lista);
		map.put("numeroContrato", "1");
		map.put("sessao", "2");
		map.put("tipoContrato", "particular");
		map.put("servico", "Psicologia");
		map.put("valorTotal", "1000");
		List<DiaConsulta> dias = new ArrayList<>();
		DiaConsulta dia1 = new DiaConsulta();
		dia1.setDiasSemana(DiasSemana.SEGUNDA);
		DiaConsulta dia2 = new DiaConsulta();
		dia2.setDiasSemana(DiasSemana.TERCA);
		dias.add(dia1);
		dias.add(dia2);
		this.planoContratoService.atualizarPlanoContratado(map);
		Servico servico = this.servicoRepository.findByServicoIgnoreCase(map.get("servico").toString());
		PlanoContratado findPlanoContratado = this.planoContratoService.consultarPlanoContratadoAtivoPorServiceIdContratoIdTipoContrato(servico.getId(),
				Long.parseLong(map.get("numeroContrato").toString()), TipoContrato.PARTICULAR);
		Assert.assertNotNull(findPlanoContratado);
		Assert.assertEquals(TipoContrato.PARTICULAR, findPlanoContratado.getTipoContrato());
	}

	/* VERIFICA O RETORNO DE LISTA DE PLANO CONTRATADO */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/DiaConsulta.sql", "/dataset/DiaConsulta-PlanoContratado.sql","/dataset/Registro.sql" })
	@Test
	public void consultarPlanoContratadoAtivoPorContratoTestMustPassVerificandoABuscaPorContrato() {
		final Double saldoMensal = 500.00;
		final String numeroContrato = "1";
		List<PlanoContratado> planoContratado = this.planoContratoService.consultarPlanoContratadoAtivoPorContrato(numeroContrato);
		
		Assert.assertNotNull(planoContratado);
		
		Assert.assertEquals(saldoMensal, planoContratado.get(1).getSaldoMensal());
		Assert.assertEquals(DiasSemana.QUINTA, planoContratado.get(0).getDiaConsulta().get(0).getDiasSemana());
		Assert.assertEquals(DiasSemana.SEXTA, planoContratado.get(0).getDiaConsulta().get(1).getDiasSemana());
		Assert.assertEquals(7, planoContratado.size());
	}
	

	/* TESTE DE BUSCA DE TODOS OS PLANOS CONTRTADOS */
	@Test
	public void consultarPlanosContratadosMustPassBuscandoTodosPlanosContratadosCadastrados() {
		final List<PlanoContratado> planoContratado = this.planoContratoService.consultarPlanosContratados();
		Assert.assertNotNull(planoContratado);
		Assert.assertEquals(6, planoContratado.size());
		;

	}
	
		/*TESTE QUE VERIFICA A DIGITAL E RETORNA TODOS PLANOS CONTRATADOS*/
		@Sql({"/dataset/truncate.sql",
			"/dataset/Servico.sql",
			"/dataset/Contrato.sql",
			"/dataset/Usuario.sql",
			"/dataset/PlanoContratado.sql",
			"/dataset/DiaConsulta.sql"})
	@Test
	public void consultarContratoPorBiometriaTestMustPassVerificaABiometriaERetornaOsTodosPlanosContratados() throws Exception  {
			List<PlanoContratado> list = this.planoContratoService.consultarContratoPorBiometria();
			Assert.assertNotNull(list);
	}
		
		@Sql({"/dataset/truncate.sql",
			"/dataset/Servico.sql",
			"/dataset/Contrato.sql",
			"/dataset/Usuario.sql",
			"/dataset/PlanoContratado.sql",
			"/dataset/DiaConsulta.sql",
			"/dataset/RegistroTestExportPlanilha.sql"})
	@Test
	public void consultarSaldoMensalMustPassVerificandoOValorExecutadoNoMes() {
		final Double valorExecutado = 1090.00;
		Double valorRetornado = this.planoContratoService.consultarSaldoMensal(2019, 12, 2L, 1L);
		Assert.assertEquals(valorExecutado, valorRetornado);
	}
		
		@Sql({"/dataset/truncate.sql",
			"/dataset/Servico.sql",
			"/dataset/Contrato.sql",
			"/dataset/Usuario.sql",
			"/dataset/PlanoContratado.sql",
			"/dataset/DiaConsulta.sql",
			"/dataset/RegistroTestExportPlanilha.sql"})
	@Test
	public void calcularValorAtendimentoMustPassVerificandoAtendimento() throws NotFoundException {
		final Double atendimento = 50.00;
		PlanoContratado plano = this.planoContratoService.consultarPlanoContratadoPorId(4L);
		plano.calcularValorAtendimento();
		Assert.assertEquals(atendimento,plano.getValorAtendimento());
	}
		
		
		@Sql({"/dataset/truncate.sql",
			"/dataset/Servico.sql",
			"/dataset/Contrato.sql",
			"/dataset/Usuario.sql",
			"/dataset/PlanoContratado.sql",
			"/dataset/DiaConsulta.sql",
			"/dataset/RegistroTestExportPlanilha.sql"})
	@Test
	public void consultarSomaTotalPlanosAtivosMustPassVerificaQuantidadePlanoAtivoParticularPlanoETotal() throws NotFoundException {
		HashMap<String, Double> map = new HashMap<>();
		final Double valorTotalParticular = 8000.00;
		final Double valorTotalPlano = 6000.00;
		final Double somaTotalPlanoParticular = valorTotalParticular + valorTotalPlano;
		map = this.planoContratoService.consultarSomaTotalPlanosAtivos();
		Assert.assertNotNull(map);
		Assert.assertEquals(valorTotalPlano, map.get("particular"));
		Assert.assertEquals(valorTotalParticular, map.get("plano"));
		Assert.assertEquals(somaTotalPlanoParticular, map.get("total"));
		
	}

	/* MUST FAIL */
		
		/*
		 * DELETA UM PLANO CONTRATADO
		 */
		 @WithUserDetails("henrique_nitatori@hotmail.com")
		  
		  @Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql",
		  "/dataset/Servico.sql", "/dataset/Contrato.sql",
		  "/dataset/PlanoContratado.sql" })
		 	
		  @Test(expected = IllegalArgumentException.class) 
		  public void deleteLogicoTestMustFailDeletandoUmPlanoContratado()
		  throws NotFoundException { 
			 this.planoContratoService.deleteLogico("99");

			 }


	/* TESTA O METODO SAVE COM PLANO CONTRATADO DUPLICADO */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void mapPlanoContratandoMustFailPlanoContratadoDuplicado() throws NotFoundException {
		Map<String, Object> map = new HashMap<>();
		map.put("horarioEntrada", "12:00");
		map.put("horarioSaida", "13:00");
		map.put("valorPlano", "120.00");
		List<String> lista = new ArrayList();
		lista.add("SEG");
		lista.add("TER");
		map.put("diaConsulta", lista);
		map.put("sessao", 2);
		map.put("numeroContrato", "1");
		map.put("tipoContrato", "plano");
		map.put("servico", "Neuropsicopedagogia");
		map.put("valorTotal", "1000");
		List<DiaConsulta> dias = new ArrayList<>();
		DiaConsulta dia1 = new DiaConsulta();
		dia1.setDiasSemana(DiasSemana.SEGUNDA);
		DiaConsulta dia2 = new DiaConsulta();
		dia2.setDiasSemana(DiasSemana.TERCA);
		dias.add(dia1);
		dias.add(dia2);
		this.planoContratoService.salvarPlanoContratado(map);
	}

	/* TESTA O METODO QUE MAPEIA UM OBJETO PLANO CONTRATADO ATRAVES DE UM HASHMAP */
	@Test(expected = MapPlanoContratadoException.class)
	public void mapPlanoContratandoMustFailFaltaDeUmCampoPreenchido() throws NotFoundException {
		Map<String, Object> map = new HashMap<>();
		map.put("horarioEntrada", "12:00");
		map.put("horarioSaida", "13:00");
		map.put("sessao", "3");
		map.put("valorPlano", "120.00");
		List<String> lista = new ArrayList();
		lista.add("SEG");
		lista.add("TER");
		map.put("diaConsulta", lista);
		map.put("numeroContrato", "1");
		map.put("servico", "Psicologia");
		map.put("valorTotal", "1000");
		List<DiaConsulta> dias = new ArrayList<>();
		DiaConsulta dia1 = new DiaConsulta();
		dia1.setDiasSemana(DiasSemana.SEGUNDA);
		DiaConsulta dia2 = new DiaConsulta();
		dia2.setDiasSemana(DiasSemana.TERCA);
		dias.add(dia1);
		dias.add(dia2);
		Servico servico = this.servicoRepository.findByServicoIgnoreCase(map.get("servico").toString());
		PlanoContratado planoContratado = this.planoContratoService.mapearPlanoContratado(map);
	}
	
		/*TESTE QUE VERIFICA A DIGITAL E RETORNA TODOS PLANOS CONTRATADOS*/
		@Sql({"/dataset/truncate.sql",
			"/dataset/Servico.sql",
			"/dataset/Contrato.sql",
			"/dataset/Usuario.sql",
			"/dataset/PlanoContratado.sql",
			"/dataset/DiaConsulta.sql"})
	@Test(expected = FingerPrintException.class)
	public void consultarContratoPorBiometriaTestMustFailVerificaABiometriaERetornaOsTodosPlanosContratados() throws Exception  {
			List<PlanoContratado> list = this.planoContratoService.consultarContratoPorBiometria();
			Assert.assertNotNull(list);
	}

}
