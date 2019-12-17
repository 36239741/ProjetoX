package com.br.projetox.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.projetox.entity.ConfiguracaoParametro;
import com.br.projetox.entity.Contrato;
import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.Registro;
import com.br.projetox.entity.Situacao;
import com.br.projetox.exception.FingerPrintException;
import com.br.projetox.repository.RegistroRepository;

import javassist.NotFoundException;

@Service
@Transactional
public class RegistroService {
	@Autowired
	private RegistroRepository registroRepository;

	@Autowired
	private ConfigParametroService configParametrosService;
	
	@Autowired
	private ContratoService contratoService;
	
	@Autowired
	private PlanoContratadoService planoContratadoService;

	public void saveHorarioEntrada(String numeroContrato , String idPlanocontratado) throws NumberFormatException, NotFoundException {
		Contrato contrato = this.contratoService.findByContractNumber(numeroContrato);
		PlanoContratado planoContratado = this.planoContratadoService.findById(Long.parseLong(idPlanocontratado));
		Registro findRegistro = this.registroRepository.findByMaxId(contrato.getNumero());
		Registro registro = new Registro();
		 registro.setContrato(contrato); 
		 registro.setPlanoContratado(planoContratado);
		 registro.setValorTotal(planoContratado.getValorPlano());
		 registro.setDataHoraEntrada(LocalDateTime.now(ZoneId.of("America/Maceio")));
		 registro.setSituacao(Situacao.ATENDIMENTO_NORMAL); 
		if(findRegistro == null) {
			 this.registroRepository.save(registro);
		}
		else {
		Duration duration = Duration.between(findRegistro.getDataHoraEntrada(), LocalDateTime.now(ZoneId.of("America/Maceio")));
		int diferencaEntraHoraDeEntradaEAgora = (int) duration.getSeconds() / 60	;
		if(diferencaEntraHoraDeEntradaEAgora > 5) {
			 this.registroRepository.save(registro);
		}
		else {
			throw new FingerPrintException("O contrato com o nome " + contrato.getNomePaciente() + " tem um registro em " + 
		findRegistro.getDataHoraEntrada().format(DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm")));
		}
		}
	}
	
	public void saveHorarioSaida(String  numeroContrato) throws NotFoundException {
		Contrato contrato  = this.contratoService.findByContractNumber(numeroContrato);
		ConfiguracaoParametro configParametro = this.configParametrosService.findConfigParametros(1L);
		Registro findRegistro = this.registroRepository.findByMaxId(contrato.getNumero());
		Duration duration = Duration.between( findRegistro.getPlanoContratado().getHorarioEntrada().plusMinutes(configParametro.getTempoSessao().getMinute())
				, LocalTime.now(ZoneId.of("America/Maceio")));
		Duration tempoTotal = Duration.between(findRegistro.getPlanoContratado().getHorarioEntrada(), LocalTime.now(ZoneId.of("America/Maceio")));
		findRegistro.setValorTotal(findRegistro.getPlanoContratado().getValorPlano());
		findRegistro.setTempoTotal(LocalTime.MIN.plusMinutes(tempoTotal.toMinutes()));
		findRegistro.setDataHoraSaida(LocalDateTime.now(ZoneId.of("America/Maceio")));
		if(duration.toMinutes() > configParametro.getTempoToleranciaAtraso().getMinute()) {
			findRegistro.setValorTotal(findRegistro.getValorTotal() + ( duration.toMinutes() * configParametro.getValorMinutoAdicional() ));
		}
		this.registroRepository.save(findRegistro);
	}
	


}
