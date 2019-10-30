package com.br.projetox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.projetox.entity.PlanoContratado;

@Repository
public interface PlanoContratoRepository extends JpaRepository<PlanoContratado, Long>{

}
