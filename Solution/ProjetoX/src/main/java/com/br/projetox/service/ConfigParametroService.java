package com.br.projetox.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.projetox.entity.ConfiguracaoParametro;
import com.br.projetox.repository.ConfigParametrosRepository;

@Service
public class ConfigParametroService {
	@Autowired
	private ConfigParametrosRepository configRepository;
	
	public ConfiguracaoParametro findConfigParametros(Long configId){
		return this.configRepository.findById(configId).get();
	}
}
