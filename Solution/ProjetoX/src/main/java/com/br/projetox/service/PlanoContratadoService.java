package com.br.projetox.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.repository.PlanoContratoRepository;

@Service
@Transactional
public class PlanoContratadoService {
	private PlanoContratoRepository planoContraRepository;
	
	public void savePlanoContratado(PlanoContratado planoContratado) {
		this.planoContraRepository.save(planoContratado);
	}
	
	@Transactional(readOnly = true)
	public PlanoContratado findPlanoContratado(long servicoId, long contratoId, int tipoContrato)  {
		PlanoContratado planoContratado = null;
		 planoContratado = this.planoContraRepository.findPlanoContratado(servicoId, contratoId, tipoContrato);
		return planoContratado;
	}
	
}
