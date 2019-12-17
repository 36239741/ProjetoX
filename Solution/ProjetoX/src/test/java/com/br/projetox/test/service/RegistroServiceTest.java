package com.br.projetox.test.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import com.br.projetox.entity.Contrato;
import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.exception.FingerPrintException;
import com.br.projetox.repository.ContratoRepository;
import com.br.projetox.repository.PlanoContratoRepository;
import com.br.projetox.service.RegistroService;

import javassist.NotFoundException;


public class RegistroServiceTest extends AbstractIntegrationTest {
	@Autowired
	private RegistroService registroService;
	
	@Autowired
	private PlanoContratoRepository planoContratadoRepository;
	
	@Autowired
	private ContratoRepository contratoRepository;
	
	/* SALVA UM HORARIO DE ENTRADA */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql" })
	@Test
	public void saveHorarioEntradaAtivoTestMustPassSalvaUmHorarioDeEntrada() throws NotFoundException {
		this.registroService.saveHorarioEntrada("1", "1");
	}
	
	/* SALVA UM HORARIO DE SAIDA */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test
	public void saveHorarioEntradaAtivoTestMustPassSalvaUmHorarioDeSaida() throws NotFoundException {
		Contrato contrato = this.contratoRepository.findByNumero("1").get();
		this.registroService.saveHorarioSaida(contrato);
	}
	
	/* MUST FAIL */
	
	/* SALVA UM HORARIO DE ENTRADA *PARA O TESTE PASSAR TEM QUE ALTERAR O HORARIO* */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql" })
	@Test(expected = FingerPrintException.class)
	public void saveHorarioEntradaAtivoTestMustFailSalvaUmHorarioDeEntradaComMenosDeCincoMinutos() throws NotFoundException {
		this.registroService.saveHorarioEntrada("1", "1");
	}
}
