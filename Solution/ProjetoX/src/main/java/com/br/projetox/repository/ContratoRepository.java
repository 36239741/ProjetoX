package com.br.projetox.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.br.projetox.entity.Contrato;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
	

	
	@EntityGraph(attributePaths = "planoContratado")
	Optional<Contrato> findByNumero (String numeroContrato);
	

	@Query("FROM Contrato contrato "
			+ "WHERE ( lower(contrato.numero) LIKE '%' || lower(:numero) || '%' OR :numero IS NULL) AND "
			+ "( lower(contrato.nomePaciente) LIKE '%' || lower(:nomePaciente) || '%' OR :nomePaciente IS NULL)")
	public Page<Contrato> findByFilters(@Param("numero") String numero,
			@Param("nomePaciente") String nomePaciente,
			Pageable pageable);
	
	@Query("FROM Contrato contrato "
			+ "WHERE ( lower(contrato.numero) LIKE '%' || lower(:numero) || '%' OR :numero IS NULL) AND "
			+ "( lower(contrato.nomePaciente) LIKE '%' || lower(:nomePaciente) || '%' OR :nomePaciente IS NULL) AND "
			+ "contrato.ativo = :ativo")
	public Page<Contrato> findByFiltersParamActive(@Param("numero") String numero,
			@Param("nomePaciente") String nomePaciente,
			Pageable pageable, @Param("ativo") Boolean ativo);
	
	@Modifying
	@Query("UPDATE Contrato contrato SET contrato.biometria = :biometria WHERE contrato.numero = :numeroContrato ")
	public void saveBiometria(@Param("numeroContrato") String numeroContrato,
							   @Param("biometria") byte[] biometria);
	


}