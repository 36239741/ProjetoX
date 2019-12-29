package com.br.projetox.test.service;

import java.time.Duration;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import com.br.projetox.entity.ConfiguracaoParametro;
import com.br.projetox.entity.Registro;
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
}
