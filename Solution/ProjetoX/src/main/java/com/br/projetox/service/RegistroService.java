package com.br.projetox.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.br.projetox.config.RunnableTask;
import com.br.projetox.entity.ConfiguracaoParametro;
import com.br.projetox.entity.Contrato;
import com.br.projetox.entity.DiasSemana;
import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.Registro;
import com.br.projetox.entity.Situacao;
import com.br.projetox.exception.RegistroException;
import com.br.projetox.repository.RegistroRepository;

import javassist.NotFoundException;

@Service
@Transactional
public class RegistroService {
	private Long plusMinutes = (long) 10;

	@Autowired
	private RegistroRepository registroRepository;

	@Autowired
	private ConfigParametroService configParametrosService;

	@Autowired
	private ContratoService contratoService;

	@Autowired
	private PlanoContratadoService planoContratadoService;

	@Autowired
	private TaskScheduler taskScheduler;

	/*
	 * @Metodo que salva o horario de entrada do paciente atraves da digital, nao
	 * permite que salva outra entrada enquanto estivar um horario em aberto
	 * 
	 * @oaram numeroContrato String - numero que costa no contrato
	 * 
	 * @param idPlanoContratado String - id do plano contratado que sera inserido o
	 * horario de entrada
	 * 
	 * @return void
	 * 
	 * @throws NumberFormatException , NotFoundException, FingerPrintException
	 */
	public Registro saveHorarioEntrada(String numeroContrato, String idPlanocontratado)
			throws NumberFormatException, NotFoundException {
		Contrato contrato = this.contratoService.findByContractNumber(numeroContrato);
		PlanoContratado planoContratado = this.planoContratadoService.findById(Long.parseLong(idPlanocontratado));
		Registro findRegistro = this.registroRepository.findByMaxId(contrato.getNumero());
		Registro registro = new Registro();
		registro.setContrato(contrato);
		registro.setPlanoContratado(planoContratado);
		registro.setValorTotal(planoContratado.getValorPlano());
		registro.setDataHoraEntrada(LocalDateTime.now(ZoneId.of("America/Maceio")));
		registro.setSituacao(Situacao.ATENDIMENTO_NORMAL);
		if (findRegistro == null) {
			return this.registroRepository.save(registro);
		} else {

			if (findRegistro.getDataHoraEntrada() == null) {
				return this.registroRepository.save(registro);
			} else {
				throw new RegistroException(
						"O contrato com o nome "
								+ contrato.getNomePaciente() + " tem um registro em " + findRegistro
										.getDataHoraEntrada().format(DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm"))
								+ "que ainda não foi fechado");
			}
		}
	}

	/*
	 * Metodo que salva o horario de saida de um paciente atraves da digital
	 * 
	 * @oaram numeroContrato String - numero que costa no contrato
	 * 
	 * @return Registro
	 * 
	 * @throws NotFoundException, FingerPrintException
	 */
	public Registro saveHorarioSaida(String numeroContrato) throws NotFoundException {
		Contrato contrato = this.contratoService.findByContractNumber(numeroContrato);
		ConfiguracaoParametro configParametro = this.configParametrosService.findConfigParametros(1L);
		Registro findRegistro = this.registroRepository.findByMaxId(contrato.getNumero());
		if (findRegistro != null && findRegistro.getDataHoraEntrada() != null
				&& findRegistro.getDataHoraSaida() == null) {
			Duration verificaValorAdicional = Duration.between(
					findRegistro.getPlanoContratado().getHorarioSaida().plusMinutes(5),
					LocalTime.now(ZoneId.of("America/Maceio")));
			if (verificaValorAdicional.toMinutes() > 0) {
				findRegistro.setValorTotal(findRegistro.getPlanoContratado().getValorPlano()
						+ (verificaValorAdicional.toMinutes() + configParametro.getTempoToleranciaAtraso().getMinute())
								* configParametro.getValorMinutoAdicional());
			} else {
				findRegistro.setValorTotal(findRegistro.getPlanoContratado().getValorPlano());
			}
			Duration tempoTotal = Duration.between(findRegistro.getPlanoContratado().getHorarioEntrada(),
					LocalTime.now(ZoneId.of("America/Maceio")));
			findRegistro.setTempoTotal(LocalTime.MIN.plusMinutes(tempoTotal.toMinutes()));
			findRegistro.setDataHoraSaida(LocalDateTime.now(ZoneId.of("America/Maceio")));
			return this.registroRepository.save(findRegistro);
		} else {
			throw new RegistroException(
					"O contrato com o nome " + contrato.getNomePaciente() + "não contem nenhum registro em aberto");
		}

	}

	/*
	 * Metodo pega o dia da semana do sistema e faz a busca dos planos ativos por
	 * dia
	 * 
	 * @return void
	 */
	public void recordTenMinutesAfterPlanTime() {
		Integer dia = LocalDate.now().getDayOfWeek().getValue();
		ConfiguracaoParametro config = this.configParametrosService.findConfigParametros(1L);
		DiasSemana diasSemana = DiasSemana.diasSemanaByOrdinal(dia);
		List<PlanoContratado> planoContratado = this.planoContratadoService.findByDiaConsulta(diasSemana);
		Registro registro = new Registro();
		for (PlanoContratado plano : planoContratado) {
			registro.setContrato(plano.getContrato());
			registro.setDataHoraEntrada(LocalDateTime.of(LocalDate.now(), plano.getHorarioEntrada()));
			registro.setDataHoraSaida(LocalDateTime.of(LocalDate.now(), plano.getHorarioSaida()));
			registro.setPlanoContratado(plano);
			registro.setTempoTotal(config.getTempoSessao());
			registro.setSituacao(Situacao.ATENDIMENTO_NORMAL);
			registro.setValorTotal(plano.getValorPlano());
			this.registroRepository.save(registro);
		}

	}

	public Page<Registro> findAllRegistro(String numeroContrato, int page, int size) {
		List<Registro> registros = this.registroRepository.findAllRegistro(numeroContrato);
		for (Registro registro : registros) {
			registro.setSituacao(Situacao.valueOf(registro.getSituacao().toString()));
		}
		PageRequest pageable = PageRequest.of(page, size);
		return new PageImpl<Registro>(registros, pageable, registros.size());
	}

	public void scheduleWork(List<PlanoContratado> planoContratado) {
		planoContratado.forEach(plano -> {
			LocalTime time = plano.getHorarioEntrada().plusMinutes(this.plusMinutes);
			CronTrigger cronTrigger = new CronTrigger("0 " + time.getMinute() + " " + time.getHour() + " * * *");
			this.taskScheduler.schedule(new RunnableTask("Horario de entrada do plano: " + plano.getHorarioEntrada()),
					cronTrigger);
		});
	}

}
