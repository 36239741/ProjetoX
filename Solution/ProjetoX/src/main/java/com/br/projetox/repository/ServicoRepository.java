package com.br.projetox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.projetox.entity.Servico;
@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
	 Servico findByServicoIgnoreCase(String name);
	 
	 
}
