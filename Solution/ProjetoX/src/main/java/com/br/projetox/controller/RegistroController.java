package com.br.projetox.controller;

import java.io.IOException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.Registro;
import com.br.projetox.service.RegistroService;

import javassist.NotFoundException;

@RestController
@RequestMapping(path = "v1/registros")
public class RegistroController {
	@Autowired
	private RegistroService registroService;

	@PostMapping(path = "/salvar-registro-entrada")
	public void saveHorarioEntrada(@RequestBody PlanoContratado plano) throws NumberFormatException, NotFoundException {
		this.registroService.salvarHorarioEntrada(plano.getContrato().getNumero(), plano.getId());

	}

	@PostMapping(path = "/salvar-registro-saida")
	public void saveHorarioSaida(@RequestBody String numeroContrato) throws NotFoundException {
		this.registroService.salvarHorarioSaida(numeroContrato);
	}

	@GetMapping(path = "/consultar-registros")
	public Page<Registro> consultarRegistros(@RequestParam(name = "page", required = true) Integer page,
			@RequestParam(name = "size", required = true) Integer size,
			@RequestParam(name = "numeroContrato") String numeroContrato) {
		return this.registroService.consultarTodosRegistrosDoContrato(numeroContrato, page, size);
	}

	@GetMapping(path = "/ausencia-profissional")
	public Registro registrarAusenciaProfissional(@RequestParam(name = "registroId") String registroId) {
		return this.registroService.registrarAusenciaDoProfisional(Long.parseLong(registroId));
	}

	@GetMapping(path = "/registrar-alteracao-servico")
	public Registro registrarAlteracaoServico(@RequestParam(name = "registroId") String registroId,
			@RequestParam(name = "valorSessao") String valorSessao) {
		Assert.notNull(valorSessao, "Informe o valor do serviço.");
		return this.registroService.registrarAlteracaoServico(Long.parseLong(registroId), Double.valueOf(valorSessao));
	}

	@GetMapping(path = "/consultar-registros-data")
	public Page<Registro> consultarRegistrosContratoPelaData(@RequestParam(name = "dataInicial") String dataInicial,
			@RequestParam(name = "dataFinal") String dataFinal,
			@RequestParam(name = "numeroContrato") String numeroContrato, @RequestParam(name = "page") int page,
			@RequestParam(name = "size") int size) {
		return this.registroService.consultarRegistrosContratoPelaData(dataInicial, dataFinal, numeroContrato, page,
				size);
	}

	@GetMapping(path = "/exportar-planilha-registro")
	public ResponseEntity<ByteArrayResource> exportarRegistrosEntradaSaida(
			@RequestParam(name = "numeroContrato") String numeroContrato) throws IOException {
		ByteArrayOutputStream planilhaRegistro = this.registroService.exportarRegistrosEntradaSaida(numeroContrato);
		HttpHeaders header = new HttpHeaders();
		header.set("Content-type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		return new ResponseEntity<>(new ByteArrayResource(planilhaRegistro.toByteArray()), header, HttpStatus.CREATED);
	}

}
