package br.com.genegouveia.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.genegouveia.domain.entity.ConfiguracaoParametro;

@Repository
public interface ConfigParametrosRepository extends JpaRepository<ConfiguracaoParametro, Long> {

}
