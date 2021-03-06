package br.com.genegouveia.domain.service.contrato;

import java.util.List;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import br.com.genegouveia.domain.entity.contrato.Servico;
import br.com.genegouveia.domain.repository.contrato.ServicoRepository;

@Service
@RemoteProxy
@Transactional
public class ServicoService {
	@Autowired
	private ServicoRepository repository;

	/*
	 * Metodo consulta os servicos pelo nome do servico ou pelo id do servico
	 * 
	 * @param servicoId
	 * 
	 * @param nomeServico
	 * 
	 * @return Servico
	 */
	public Servico consultarServicosPorNomeServicoServicoId(Long servicoId, String nomeServico) {
		Servico servico = this.repository.consultarContratosPorNomeservicoServicoId(nomeServico, servicoId);
		Assert.notNull(servico, "Nenhum servico encontrado");
		return servico;
	}
	

	/*
	 * Metodo lista todos servicos cadastrados 
	 * return List<Servico>
	 */
	public List<Servico> consultarTodosServicos() {
		return this.repository.findAll();
	}


}
