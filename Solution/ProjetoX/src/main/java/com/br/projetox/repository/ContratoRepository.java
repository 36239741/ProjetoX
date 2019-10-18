package com.br.projetox.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.projetox.entity.Contrato;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
	Optional<Contrato> findByNumero (int numeroContrato);
}
