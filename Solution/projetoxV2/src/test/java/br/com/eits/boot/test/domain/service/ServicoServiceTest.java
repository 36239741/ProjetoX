package br.com.eits.boot.test.domain.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import br.com.eits.boot.test.domain.AbstractIntegrationTests;
import br.com.genegouveia.domain.entity.contrato.Servico;
import br.com.genegouveia.domain.service.ServicoService;

public class ServicoServiceTest extends AbstractIntegrationTests {
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
