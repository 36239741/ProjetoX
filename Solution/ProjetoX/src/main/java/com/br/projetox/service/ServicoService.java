package com.br.projetox.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.br.projetox.entity.Servico;
import com.br.projetox.repository.ServicoRepository;

import javassist.NotFoundException;

@Service
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
