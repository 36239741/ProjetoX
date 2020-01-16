package com.br.projetox.infrastructure.scheduling;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.br.projetox.entity.Registro;
import com.br.projetox.service.RegistroService;

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

}