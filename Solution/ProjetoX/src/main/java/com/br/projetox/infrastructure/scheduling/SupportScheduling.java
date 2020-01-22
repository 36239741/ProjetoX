package com.br.projetox.infrastructure.scheduling;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.br.projetox.entity.DiasSemana;
import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.Registro;
import com.br.projetox.service.PlanoContratadoService;
import com.br.projetox.service.RegistroService;

import javassist.NotFoundException;

@Lazy(false)
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SupportScheduling {

	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	private final Map<Long, ScheduledFuture<?>> futureSupportJobsSaidaAutomatica = new ConcurrentHashMap<>();
	private final Map<Long, ScheduledFuture<?>> futureSupportJobsAusenciaPaciente = new ConcurrentHashMap<>();
	/**
	 *
	 */
	@Autowired
	private TaskScheduler taskScheduler;
	
	@Autowired
	private RegistroService registroService;
	
	@Autowired
	private PlanoContratadoService planoService;

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTOR
	 *-------------------------------------------------------------------*/
	public SupportScheduling() {
	}

	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@PostConstruct
	public void postConstruct() {
		System.out.println("imprime");
	}

	public void scheduleSaidaAutomatica(final Registro registro) {
		LocalDate dataAtual = LocalDate.now();
		LocalTime horaExcecucao = registro.getPlanoContratado().getHorarioSaida();
		Date date = Date.from(dataAtual.atTime(horaExcecucao).atZone(ZoneId.systemDefault()).toInstant());
		
		// Cria um thread para ser executada quando a data for atingida
		final ScheduledFuture<?> future = this.taskScheduler.schedule(new Runnable() {
			@Override
			public void run() {
				registroService.registrarSaidaAutomatica(registro);
			}
		}, date);

		this.futureSupportJobsSaidaAutomatica.put(registro.getId(), future);
	}
	
	public void scheduleRegistroAutomaticoAusenciaPaciente(final PlanoContratado plano) {
		LocalDate dataAtual = LocalDate.now();
		//adiciona 10 minutos ao horário de entrada
		LocalTime horaExcecucao = plano.getHorarioEntrada().plusMinutes(10);
		Date date = Date.from(dataAtual.atTime(horaExcecucao).atZone(ZoneId.systemDefault()).toInstant());
		
		// Cria um thread para ser executada quando a data for atingida
		final ScheduledFuture<?> future = this.taskScheduler.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					registroService.registrarAusenciaPacienteAutomaticamente(plano.getId());
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, date);

		this.futureSupportJobsSaidaAutomatica.put(plano.getId(), future);
	}
	
	
	
	/**
	 * Este serviço roda toda a madrugada agendando as threads que irão executar
	 * a criação automática de registros de ausência de pacientes no dia seguinte
	 */
	@Scheduled(cron = "0 0 3 * * ?")
	public void agendarRegistroAutomaticoAusenciaPaciente() {

	    //descrobrir que dia da semana é hoje
	    LocalDate dataAtual = LocalDate.now();
	    DayOfWeek diaSemanaAtual = dataAtual.getDayOfWeek();
	    DiasSemana diaSemanaTrauzido;
	    switch (diaSemanaAtual) {
	    case SUNDAY:
	    	diaSemanaTrauzido = DiasSemana.DOMINGO;
			break;
	    case MONDAY:
	    	diaSemanaTrauzido = DiasSemana.SEGUNDA;
			break;
	    case TUESDAY:
	    	diaSemanaTrauzido = DiasSemana.TERCA;
			break;
	    case WEDNESDAY:
	    	diaSemanaTrauzido = DiasSemana.QUARTA;
			break;
	    case THURSDAY:
	    	diaSemanaTrauzido = DiasSemana.QUINTA;
			break;
	    case FRIDAY:
	    	diaSemanaTrauzido = DiasSemana.SEXTA;
			break;
		case SATURDAY:
			diaSemanaTrauzido = DiasSemana.SABADO;
			break;

		default:
			diaSemanaTrauzido = DiasSemana.DOMINGO;
			break;
		}
	    
	    List<PlanoContratado> planosAtivos =  this.planoService.findByDiaConsulta(diaSemanaTrauzido);
	    
	    for (PlanoContratado planoContratado : planosAtivos) {
			this.scheduleRegistroAutomaticoAusenciaPaciente(planoContratado);
		}
	}

}