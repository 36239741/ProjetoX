package com.br.projetox.test.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import com.br.projetox.entity.Servico;
import com.br.projetox.service.ServicoService;

public class ServicoServiceTest extends AbstractIntegrationTest {
	@Autowired
	private ServicoService servicoService;
	
	/* Teste verifica a consulta pelo nome do servico */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test()
	public void consultarServicosPorNomeServicoServicoIdMustPassConsultandoServicoPeloNome() {
		Servico servico = this.servicoService.consultarServicosPorNomeServicoServicoId(null,"Psicologia");
		Assert.assertNotNull(servico);
		Assert.assertEquals("Psicologia", servico.getServico());
	}
	
	/* Teste verifica a consulta pelo id do servico */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test()
	public void consultarServicosPorNomeServicoServicoIdMustPassConsultandoServicoPeloID() {
		Servico servico = this.servicoService.consultarServicosPorNomeServicoServicoId(2L,"");
		Assert.assertNotNull(servico);
		Assert.assertEquals("Psicologia", servico.getServico());
	}
	
//	*************************************************************** Must Fail *****************************************************************************
	/* Teste verifica a consulta pelo id do servico inexistente */
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/Registro.sql","/dataset/Config.sql" })
	@Test(expected = IllegalArgumentException.class)
	public void consultarServicosPorNomeServicoServicoIdMustFailConsultandoServicoPeloID() {
		Servico servico = this.servicoService.consultarServicosPorNomeServicoServicoId(5L,"");
		Assert.assertNotNull(servico);
		Assert.assertEquals("Psicologia", servico.getServico());
	}
}
