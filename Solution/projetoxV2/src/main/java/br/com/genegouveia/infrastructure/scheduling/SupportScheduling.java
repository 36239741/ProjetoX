package br.com.genegouveia.infrastructure.scheduling;

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

import br.com.genegouveia.domain.entity.contrato.DiasSemana;
import br.com.genegouveia.domain.entity.contrato.PlanoContratado;
import br.com.genegouveia.domain.entity.contrato.Registro;
import br.com.genegouveia.domain.service.contrato.PlanoContratadoService;
import br.com.genegouveia.domain.service.contrato.RegistroService;
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
		
	}

	/**
	 * Esse serviço executa automaticamente no horário agendado para a saída de um atendimento
	 * A fim de registrar automaticamente o encerramento do atendimento
	 * Agendado ao registrar a entrada de um paciente
	 * SUBSTITUÍDO PELO AGENDAMENTO NOTURNO
	 * @param registro
	 */
	/*public void scheduleSaidaAutomatica(final Registro registro) {
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
	}*/
	
	/**
	 * Esse serviço executa automaticamente todas as noites verificando os atendimentos que não foram fechados
	 * A fim de registrar automaticamente o encerramento do atendimento
	 * Substituindo a execução do serviço scheduleSaidaAutomatica(final Registro registro)
	 */
	@Scheduled(cron = "0 0 17 * * ?")
	public void scheduleSaidaAutomatica() {
		//recupera todos os registros abertos
	    List<Registro> registrosAbertos = this.registroService.listRegistrosAbertos();
	    //para cada registro aberto realiza o fechamento automático
	    for (Registro registro : registrosAbertos) {
	    	registroService.registrarSaidaAutomatica(registro);
		}
		
	}
	
	/**
	 * Esse serviço executa automaticamente após 10 minutos do horário de entrada de um atendimento
	 * A fim de registrar a ausência do paciente caso o mesmo não tenha comparecido ao atendimento
	 * @param plano
	 */
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
	    
	    List<PlanoContratado> planosAtivos =  this.planoService.consultarContratoPorDiasSemana(diaSemanaTrauzido);
	    
	    for (PlanoContratado planoContratado : planosAtivos) {
			this.scheduleRegistroAutomaticoAusenciaPaciente(planoContratado);
		}
	}

}