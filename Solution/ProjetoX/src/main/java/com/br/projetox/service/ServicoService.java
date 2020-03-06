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
	
	public Servico consultarServicoPeloNome(String servico) {
		Servico returnServico = this.repository.findByServicoIgnoreCase(servico);
		Assert.notNull(returnServico, "Nenhum servico encontrado");
		return returnServico;
	}
	public List<Servico> consultarTodosServicos(){
		return this.repository.findAll();
	}
	public Servico consultarServico(Long id) throws NotFoundException {
		return this.repository.findById(id).orElseThrow(() -> new NotFoundException("Não foi encontrado o servico com o id: " + id));
	}
}
