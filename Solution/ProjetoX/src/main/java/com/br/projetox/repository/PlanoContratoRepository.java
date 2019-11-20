package com.br.projetox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.TipoContrato;

@Repository
public interface PlanoContratoRepository extends JpaRepository<PlanoContratado, Long>{
		
	@Query("FROM PlanoContratado plano "
			+ "WHERE "
			+ "plano.servico.id = :servicoId "
			+ "AND plano.contrato.id = :contratoId "
			+ "AND plano.tipoContrato = :tipoContrato "
			+ "AND plano.ativo = true")
	 PlanoContratado findPlanoContratadoAtivoByContratoAndServicoAndTipoContrato(@Param("servicoId") long servicoId ,@Param("contratoId") long contratoId, @Param("tipoContrato") TipoContrato tipoContrato);
	
	@Query("FROM PlanoContratado plano "
			+ "WHERE "
			+ "plano.servico.id = :servicoId "
			+ "AND plano.tipoContrato = :tipoContrato ")
	PlanoContratado findbyContractTypeAndServiceId(@Param("tipoContrato")TipoContrato tipoContrato, @Param("servicoId") long servicoId);
	
	@Query("FROM PlanoContratado plano "
			+ "WHERE "
			+ "plano.servico.id = :servicoId ")
	List<PlanoContratado> findByContratoId(@Param("servicoId") long servicoId);
	
	@Modifying()
	@Query("UPDATE PlanoContratado plano "
			+ "SET plano.ativo = false "
			+ "WHERE "
			+ "plano.servico.id = :servicoId "
			+ "AND plano.contrato.id = :contratoId "
			+ "AND plano.tipoContrato = :tipoContrato ")
	 void deleteLogical(@Param("servicoId") long servicoId ,@Param("contratoId") long contratoId, @Param("tipoContrato") TipoContrato tipoContrato);
	
}
