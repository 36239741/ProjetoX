package br.com.genegouveia.application.restful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.genegouveia.domain.entity.ConfiguracaoParametro;
import br.com.genegouveia.domain.service.ConfigParametroService;
@Component
@RestController()
@RequestMapping(path = "v1/config-parametros")
public class ConfigParametroResource {
	@Autowired
	private ConfigParametroService configService;
	
	@GetMapping(path = "/{configId}")
	public ResponseEntity<?> consultarConfiguracao(@PathVariable String configId){
		ConfiguracaoParametro configParametros = this.configService.consultarConfiguracaoPorId(Long.parseLong(configId));
		return ResponseEntity.ok(configParametros);
	}
}
	