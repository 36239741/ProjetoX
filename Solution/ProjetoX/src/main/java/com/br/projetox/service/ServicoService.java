package com.br.projetox.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.br.projetox.entity.Servico;
import com.br.projetox.repository.ServicoRepository;

@Service
@Transactional
public class ServicoService {
	@Autowired
	private ServicoRepository repository;
	
	public Servico findServico(String servico) {
		Servico returnServico = this.repository.findByServicoIgnoreCase(servico);
		Assert.notNull(returnServico, "Nenhum servico encontrado");
		return returnServico;
	}
	public List<Servico> listServicos(){
		return this.repository.findAll();
	}
}
