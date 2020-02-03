package com.br.projetox.test.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import com.br.projetox.entity.Relatorio;
import com.br.projetox.service.RelatorioService;

public class RelatorioServiceTest extends AbstractIntegrationTest{
	@Autowired
	private RelatorioService relatorioService;
	
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/RegistroTestExportPlanilha.sql","/dataset/Config.sql" })
	@Test
	public void valorExecutadoMustPassTestandoOValorExecutadoDoContrato() {
		final Double valorExecutadoTest = 1090.00;
		final Double valorExecutado = this.relatorioService.valorExecutado(2019,12,"1");
		
		Assert.assertEquals(valorExecutadoTest, valorExecutado);
	}
	
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/RegistroTestExportPlanilha.sql","/dataset/Config.sql" })
	@Test
	public void calculoDaDiferencaMustPassCalculandoADiferencaEntreValorExecutadoEValorContratado() {
		final Double valorDaDiferenca = -90.00;
		final Double diferenca = this.relatorioService.calculoDaDiferenca(1090.00D, 1000.00D);
		Assert.assertEquals(valorDaDiferenca, diferenca);
	}
	
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/RegistroTestExportPlanilha.sql","/dataset/Config.sql" })
	@Test
	public void montarEntidadeRelatorioMustPassMontandoAEntidadeRelatorio() {
		List<Relatorio> relatorios = this.relatorioService.montarEntidadeRelatorio(2019, 12);
		
		Assert.assertEquals(3, relatorios.size());
	}
	
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/RegistroTestExportPlanilha.sql","/dataset/Config.sql" })
	@Test
	public void montarEntidadeRelatorioMustPassVerificandoDadosRetornados() {
		final Double valorContrato = 1000.00;
		final Double valorExecutado = 1090.00;
		final Double valorDiferenca = -90.00;
		
		List<Relatorio> relatorios = this.relatorioService.montarEntidadeRelatorio(2019, 12);

		Assert.assertEquals(3, relatorios.size());
		
		for(Relatorio relatorio: relatorios) {
			if(relatorio.getNumeroContrato().equals("1")) {
				Assert.assertEquals("Henrique Nitatori", relatorio.getNomePaciente());
				Assert.assertEquals("1", relatorio.getNumeroContrato());
				Assert.assertEquals(valorContrato, relatorio.getValorContratado());
				Assert.assertEquals(valorExecutado, relatorio.getValorExecutado());
				Assert.assertEquals(valorDiferenca, relatorio.getDiferenca());
			}
			
		}
		
	}
	
	@WithUserDetails("henrique_nitatori@hotmail.com")
	@Sql({ "/dataset/truncate.sql", "/dataset/Usuario.sql", "/dataset/Servico.sql", "/dataset/Contrato.sql",
			"/dataset/PlanoContratado.sql","/dataset/RegistroTestExportPlanilha.sql","/dataset/Config.sql" })
	@Test
	public void createPlanilhaRelatorioMustPassCriandoUmaPlanilhaDeRelatorio() throws IOException {
		
		ByteArrayOutputStream bytePLanilha = this.relatorioService.createPlanilhaRelatorio(2019, 12);
		File file = new File("teste.xlsx");
		FileOutputStream createPlanilha = new FileOutputStream(file);
		createPlanilha.write(bytePLanilha.toByteArray());
		createPlanilha.flush();
		createPlanilha.close();
		
	}

	
	/* MUST FAIL */
	

}
