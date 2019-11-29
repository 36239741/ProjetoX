package com.br.projetox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.projetox.entity.ConfiguracaoParametro;

@Repository
public interface ConfigParametrosRepository extends JpaRepository<ConfiguracaoParametro, Long> {

}
