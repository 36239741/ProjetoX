package br.com.genegouveia.domain.service;

import javax.transaction.Transactional;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.genegouveia.domain.entity.ConfiguracaoParametro;
import br.com.genegouveia.domain.repository.ConfigParametrosRepository;

@RemoteProxy
@Transactional
@Service
public class ConfigParametroService {
	@Autowired
	private ConfigParametrosRepository configRepository;
	
	/*
	 * Metodo consulta as configuracoes do sistema
	 * 
	 * @param configId
	 * 
	 * @return ConfiguracaoParametro
	 */
	public ConfiguracaoParametro consultarConfiguracaoPorId(Long configId){
		return this.configRepository.findById(configId).get();
	}
}
