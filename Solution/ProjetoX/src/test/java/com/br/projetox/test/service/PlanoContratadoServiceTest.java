package com.br.projetox.test.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import com.br.projetox.entity.Contrato;
import com.br.projetox.entity.DiaConsulta;
import com.br.projetox.entity.DiasSemana;
import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.Servico;
import com.br.projetox.entity.TipoContrato;
import com.br.projetox.exception.DuplicatePlanoContratadoException;
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
	public void findPlanoContratadoAtivoTestMustPass() throws NotFoundException {
		final Long servicoId = this.servicoRepository.findById(1L).orElse(null).getId();
		final Long contratoId = this.contratoRepository.findById(1L).orElse(null).getId();

		final PlanoContratado plano = this.planoContratoService.findPlanoContratadoAtivo(servicoId, contratoId,
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
	  
	  @Test public void deleteLogicalTestMustPassDeletandoUmPlanoContratado()
	  throws NotFoundException { 
		 this.planoContratoService.deleteLogical("1");
		 PlanoContratado plano = this.planoContratadoRepository.findById(Long.parseLong("1")).get();
		 Assert.assertEquals(false, plano.getAtivo());
	  }
	 

	/*
	 * VERIFICA O RETORNO DA FUNCAO QUE CALCULA O TOTAL DO VALOR DO TIPO PLANO E
	 * PARTICULAR
	 */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql" })
	@Test
	public void findTotalActiveContractsMustPassVerificandoRetornoDaFuncao() throws NotFoundException {
		final Double particular = 4000.00;
		final Double plano = 2000.00;
		Map<String, Double> map = this.planoContratoService.findTotalActiveContracts();

		Assert.assertNotNull(map);
		Assert.assertEquals(map.get("Particular"), plano);
		Assert.assertEquals(map.get("Plano"), particular);

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
		PlanoContratado planoContratado = this.planoContratoService.mapPlanoContratado(map);
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
		this.planoContratoService.savePlanoContratato(map);
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
		map.put("servico", "Psicologia");
		map.put("valorTotal", "1000");
		List<DiaConsulta> dias = new ArrayList<>();
		DiaConsulta dia1 = new DiaConsulta();
		dia1.setDiasSemana(DiasSemana.SEGUNDA);
		DiaConsulta dia2 = new DiaConsulta();
		dia2.setDiasSemana(DiasSemana.TERCA);
		dias.add(dia1);
		dias.add(dia2);
		this.planoContratoService.updatePlanoContrato(map);
		Servico servico = this.servicoRepository.findByServicoIgnoreCase(map.get("servico").toString());
		PlanoContratado findPlanoContratado = this.planoContratoService.findPlanoContratadoAtivo(servico.getId(),
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
		this.planoContratoService.updatePlanoContrato(map);
		Servico servico = this.servicoRepository.findByServicoIgnoreCase(map.get("servico").toString());
		PlanoContratado findPlanoContratado = this.planoContratoService.findPlanoContratadoAtivo(servico.getId(),
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
		this.planoContratoService.updatePlanoContrato(map);
		Servico servico = this.servicoRepository.findByServicoIgnoreCase(map.get("servico").toString());
		PlanoContratado findPlanoContratado = this.planoContratoService.findPlanoContratadoAtivo(servico.getId(),
				Long.parseLong(map.get("numeroContrato").toString()), TipoContrato.PARTICULAR);
		Assert.assertNotNull(findPlanoContratado);
		Assert.assertEquals(TipoContrato.PARTICULAR, findPlanoContratado.getTipoContrato());
	}

	/* VERIFICA O RETORNO DE LISTA DE PLANO CONTRATADO */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql" })
	@Test
	public void findbyContractIdTestMustPassVerificandoABuscaPorContrato() {
		final String numeroContrato = "1";
		List<PlanoContratado> planoContratado = this.planoContratoService.findAllPlanoContratadoByContratoId(numeroContrato);

		Assert.assertNotNull(planoContratado);
		Assert.assertEquals(1, planoContratado.size());
	}

	/* TESTE DE BUSCA DE TODOS OS PLANOS CONTRTADOS */
	@Test
	public void findAllMustPass() {
		final List<PlanoContratado> planoContratado = this.planoContratoService.findAll();
		Assert.assertNotNull(planoContratado);
		Assert.assertEquals(0, planoContratado.size());
		;

	}


	/* MUST FAIL */

	/* TESTA O METODO SAVE COM PLANO CONTRATADO DUPLICADO */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql" })
	@Test(expected = DuplicatePlanoContratadoException.class)
	public void mapPlanoContratandoMustFailPlanoContratadoDuplicado() throws NotFoundException {
		Map<String, Object> map = new HashMap<>();
		map.put("horarioEntrada", "12:00");
		map.put("horarioSaida", "13:00");
		map.put("valorPlano", "120.00");
		List<String> lista = new ArrayList();
		lista.add("SEG");
		lista.add("TER");
		map.put("diaConsulta", lista);
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
		this.planoContratoService.savePlanoContratato(map);
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
		PlanoContratado planoContratado = this.planoContratoService.mapPlanoContratado(map);
	}

}
