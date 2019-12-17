package com.br.projetox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.br.projetox.entity.Contrato;
import com.br.projetox.entity.Registro;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long>{
	
	Registro findByContrato(Contrato contrato);
	
	@Query("FROM Registro registro WHERE registro.contrato.numero = :numeroContrato AND registro.id = "
			+ "(SELECT max(registro.id) FROM Registro registro)")
	Registro findByMaxId(@Param("numeroContrato") String contratoId);
}
