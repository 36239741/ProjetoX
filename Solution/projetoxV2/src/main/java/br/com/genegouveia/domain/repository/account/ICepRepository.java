package br.com.genegouveia.domain.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.genegouveia.domain.entity.account.Cep;

@Repository
public interface ICepRepository extends JpaRepository<Cep, Long>
{
}
