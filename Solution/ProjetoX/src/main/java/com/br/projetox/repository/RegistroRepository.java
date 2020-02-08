package com.br.projetox.repository;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.br.projetox.entity.Contrato;
import com.br.projetox.entity.Registro;
import com.br.projetox.entity.Situacao;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long>{
	
	Registro findByContrato(Contrato contrato);
	
	@Query("FROM Registro registro WHERE registro.contrato.numero = :numeroContrato AND registro.id = "
			+ "(SELECT max(registro.id) FROM Registro registro) AND registro.contrato.ativo = true")
	Registro findByMaxId(@Param("numeroContrato") String contratoId);
	
	@Query("FROM Registro registro WHERE registro.contrato.numero = :numeroContrato")
	public Page<Registro> findAllRegistro(@Param("numeroContrato") String numeroContrato, Pageable pageable);
	
	@Query("FROM Registro registro WHERE registro.contrato.numero = :numeroContrato")
	public List<Registro> findAllRegistroList(@Param("numeroContrato") String numeroContrato);
	
	@Query("FROM Registro registro WHERE registro.contrato.numero = :numeroContrato AND registro.dataHoraEntrada "
			+ "BETWEEN :dataInicial AND :dataFinal")
	public Page<Registro> findByDate(@Param("dataInicial") LocalDateTime dataInicial,
			@Param("dataFinal") LocalDateTime dataFinal,
			@Param("numeroContrato") String numeroContrato,
			Pageable pagebale);
	
	@Query("FROM Registro registro WHERE registro.planoContratado.id = :planoId AND registro.dataHoraEntrada "
			+ "BETWEEN :dataInicial AND :dataFinal")
	public Page<Registro> findByDateAndPLanoId(@Param("dataInicial") LocalDateTime dataInicial,
			@Param("dataFinal") LocalDateTime dataFinal,
			@Param("planoId") Long planoId,
			Pageable pagebale);
	
	
	@Query("FROM Registro registro WHERE registro.planoContratado.id = :planoId AND registro.id = "
			+ "(SELECT max(registro.id) FROM Registro registro)")
	Registro findByPlanoContratadoAndMaxId(@Param("planoId") Long planoId);

	@Query("FROM Registro registro "
			+ "WHERE registro.dataHoraSaida IS NULL "
			+ "AND registro.dataHoraEntrada IS NOT NULL "
			+ "AND registro.situacao = " + Situacao.SITUACAO_ATENDIMENTO_NORMAL)
	List<Registro> findAbertosAndAtendimentoNormal();
}
