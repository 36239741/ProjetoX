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
	 * Metodo consulta um servico pelo seu nome
	 * 
	 * @param nomeServico
	 * 
	 * @return Servico
	 */
	public Servico consultarServicoPeloNome(String servico) {
		Servico consultarServico = this.repository.findByServicoIgnoreCase(servico);
		Assert.notNull(consultarServico, "Nenhum servico encontrado");
		return consultarServico;
	}

	/*
	 * Metodo lista todos servicos cadastrados 
	 * return List<Servico>
	 */
	public List<Servico> consultarTodosServicos() {
		return this.repository.findAll();
	}

	/*
	 * Metodo consulta um servico em especifico
	 * 
	 * @param id
	 * 
	 * @return Servico
	 */
	public Servico consultarServicoPorId(Long id) throws NotFoundException {
		return this.repository.findById(id)
				.orElseThrow(() -> new NotFoundException("Não foi encontrado o servico com o id: " + id));
	}
}
