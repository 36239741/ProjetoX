package com.br.projetox.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.br.projetox.entity.DiasSemana;
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
	 PlanoContratado consultarPlanoContratadoAtivoPorServiceIdContratoIdTipoContrato(@Param("servicoId") long servicoId ,@Param("contratoId") long contratoId, @Param("tipoContrato") TipoContrato tipoContrato);
	

	@Query("FROM PlanoContratado plano "
			+ "WHERE "
			+ "plano.contrato.numero = :numeroContrato "
			+ "AND plano.ativo = true")
	List<PlanoContratado> consultarPlanoContratadoPorNumeroContrato(@Param("numeroContrato") String numeroContrato);
	
	@Modifying
	@Query("UPDATE PlanoContratado plano "
			+ "SET plano.ativo = false "
			+ "WHERE "
			+ "plano.id = :planoId")
	 Integer deleteLogico(@Param("planoId") long planoId );
	
	@Query("FROM PlanoContratado plano "
			+ "JOIN plano.diaConsulta diaConsulta "
			+ "WHERE "
			+ "plano.ativo = true AND diaConsulta.diasSemana = :diasSemana ")
	List<PlanoContratado> consultarPlanoContratadoPorDiasSemana(@Param("diasSemana") DiasSemana diasSemana);
	
	@Query("FROM PlanoContratado plano "
			+ "where plano.ativo = true")
	List<PlanoContratado> consultarPlanosContratadosAtivos();
	

}
