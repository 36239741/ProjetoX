package br.com.genegouveia.domain.repository;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.genegouveia.domain.entity.contrato.Contrato;
import br.com.genegouveia.domain.entity.contrato.Registro;
import br.com.genegouveia.domain.entity.contrato.Situacao;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long>{
	
	
	Registro findByPlanoContratadoContrato(Contrato contrato);
	
	@Query("FROM Registro registro WHERE registro.id = (SELECT max(id) FROM Registro registro where registro.planoContratado.contrato.numero = :numeroContrato)")
	Registro consultarUltimoRegistroContrato(@Param("numeroContrato") String numeroContrato);
	
	@Query("FROM Registro registro WHERE registro.planoContratado.contrato.numero = :numeroContrato")
	public Page<Registro> consultarRegistros(@Param("numeroContrato") String numeroContrato, Pageable pageable);
	
	
	@Query("FROM Registro registro WHERE registro.planoContratado.contrato.numero = :numeroContrato AND registro.dataHoraEntrada "
			+ "BETWEEN :dataInicial AND :dataFinal")
	public Page<Registro> consultarRegistroPorDataInicialFinalNumeroContrato(@Param("dataInicial") LocalDateTime dataInicial,
			@Param("dataFinal") LocalDateTime dataFinal,
			@Param("numeroContrato") String numeroContrato,
			Pageable pagebale);
	
	@Query("FROM Registro registro WHERE registro.planoContratado.id = :planoId AND registro.planoContratado.contrato.id = :contratoId AND registro.dataHoraEntrada "
			+ " BETWEEN :dataInicial AND :dataFinal")
	public Page<Registro> consultarRegistroPorDataInicialFinalPlanoIdContratoId(@Param("dataInicial") LocalDateTime dataInicial,
			@Param("dataFinal") LocalDateTime dataFinal,
			@Param("planoId") Long planoId,
			@Param("contratoId") Long contratoId,
			Pageable pagebale);

	
	@Query("FROM Registro registro WHERE  registro.id = "
			+ "(SELECT max(id) FROM Registro registro where registro.planoContratado.id = :planoId)")
	Registro consultarUltimoRegistroDoPlano(@Param("planoId") Long planoId);

	@Query("FROM Registro registro "
			+ "WHERE registro.dataHoraSaida IS NULL "
			+ "AND registro.dataHoraEntrada IS NOT NULL "
			+ "AND registro.situacao = " + Situacao.SITUACAO_ATENDIMENTO_NORMAL)
	List<Registro> consultarRegistrosAbertoAtendimentoNormal();
}
