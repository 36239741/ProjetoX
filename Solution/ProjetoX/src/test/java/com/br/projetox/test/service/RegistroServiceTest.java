package com.br.projetox.test.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

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
	
	
	/* SALVA UM HORARIO DE ENTRADA */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql" })
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
	public void exchangeOfContractStatusTestMustPasstTestaATrocaDeServicoTipoContratoPlanoServicoValorMenorDoQueADoPlano() throws NotFoundException {
		final Double valorPlano = 45.00;
		final Double valorTotal = 2035.00;

		Registro registro = this.registroService.exchangeOfContractStatus("TROCA_DE_SERVICO", 4L, "Terapia Ocupacional",valorPlano);
		
		Assert.assertNotNull(registro);
		Assert.assertEquals(Situacao.TROCA_DE_SERVICO, registro.getSituacao());
		Assert.assertEquals(valorTotal, registro.getPlanoContratado().getValorTotal());

	}
	
	/* TESTA A TROCA DA SITUACAO DO CONTRATO PARA AUSENCIA DO PROFISSIONAL */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void exchangeOfContractStatusTestMustPasstTestaATrocaDeServicoTipoContratoPlanoServicoValorMaiorDoQueADoPlano() throws NotFoundException {
		final Double valorPlano = 45.00;
		final Double valorTotal = 1045.00;
		Registro registro = this.registroService.exchangeOfContractStatus("TROCA_DE_SERVICO", 3L, "Terapia Ocupacional",valorPlano);
		
		Assert.assertNotNull(registro);
		Assert.assertEquals(Situacao.TROCA_DE_SERVICO, registro.getSituacao());
		Assert.assertEquals(valorTotal, registro.getPlanoContratado().getValorTotal());

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
}
