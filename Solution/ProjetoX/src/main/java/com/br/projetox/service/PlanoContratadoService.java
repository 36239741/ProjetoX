package com.br.projetox.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.TipoContrato;
import com.br.projetox.repository.PlanoContratoRepository;

@Service
@Transactional
public class PlanoContratadoService {
	
	@Autowired
	private PlanoContratoRepository planoContraRepository;
	
	
	/*
	 * ==================== TESTES DE SALVAR PLANO CONTRATADO ==============================
	 */
	public void savePlanoContratado(PlanoContratado planoContratado) {
		this.planoContraRepository.save(planoContratado);
	}
	
	public List<PlanoContratado> findAll() {
		return this.planoContraRepository.findAll();
	}
	
	
	public PlanoContratado findbyContractTypeAndServiceId(TipoContrato tipoContrato, long serviceId){
		return this.planoContraRepository.findbyContractTypeAndServiceId(tipoContrato, serviceId);
	}
	
	public List<PlanoContratado> findByContractId(Long contratoId){
		return this.planoContraRepository.findByContratoId(contratoId);
	}
	
	/*
	 * ==================== TESTES DE BUSCAR PLANOS CONTRATADOS ATIVOS ==============================
	 */
	@Transactional(readOnly = true)
	public PlanoContratado findPlanoContratadoAtivo(long servicoId, long contratoId, TipoContrato tipoContrato)  {
		return this.planoContraRepository.findPlanoContratadoAtivoByContratoAndServicoAndTipoContrato(servicoId, contratoId, tipoContrato);
	}
	
}
