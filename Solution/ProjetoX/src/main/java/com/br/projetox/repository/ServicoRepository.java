package com.br.projetox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.br.projetox.entity.Servico;
@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
	 Servico findByServicoIgnoreCase(String name);
	 
	 
	 @Query("FROM Servico servico "
				+ "WHERE servico.servico = :nomeServico OR :nomeServico IS NULL OR "
				+ "servico.id = :servicoId ")
	 public Servico consultarContratosPorNomeservicoServicoId(@Param("nomeServico") String nomeServico, @Param("servicoId") Long servicoId);
	 
}
