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
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.br.projetox.config.RunnableTask;
import com.br.projetox.entity.ConfiguracaoParametro;
import com.br.projetox.entity.Contrato;
import com.br.projetox.entity.DiasSemana;
import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.Registro;
import com.br.projetox.entity.Servico;
import com.br.projetox.entity.Situacao;
import com.br.projetox.entity.TipoContrato;
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
	private ServicoService servicoService;

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

		if (findRegistro == null || findRegistro.getDataHoraSaida() != null ) {
			return this.registroRepository.save(registro);
		}
		else {
				throw new RegistroException(
						"O contrato com o nome "
								+ contrato.getNomePaciente() + " tem um registro em " + findRegistro
										.getDataHoraEntrada().format(DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm"))
								+ " que ainda não foi fechado");
			}
		}
	
	/*
	 * Metodo que busca os registros pela data
	 * 
	 * @Param dataInicial String - data de inicio para busca
	 * 
	 * @Param dataFinal String - data final da busca
	 * 
	 * @Param contratoId String - id do contrato
	 * 
	 * @Param page int - numero da pagina de retorno
	 * 
	 * @Param size int - tamanho da pagina de retorno
	 * 
	 * @return page<Registro>
	 */
 	public Page<Registro> findByDate(String dataInicial, String dataFinal, String contratoId, int page, int size) {
		if(dataInicial.isEmpty() == false && dataFinal.isEmpty() == false && contratoId.isEmpty() == false) {
			Pageable pagebale = PageRequest.of(page, size);
			return this.registroRepository.findByDate(LocalDateTime.parse(dataInicial+"T00:00:00"),
					LocalDateTime.parse(dataFinal+"T00:00:00"),
					Long.parseLong(contratoId), pagebale);
		}
		else {
			throw new RegistroException("Campos obrigatório não preenchidos");
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
			Duration tempoTotal = Duration.between(findRegistro.getDataHoraEntrada().toLocalTime(),
					LocalTime.now(ZoneId.of("America/Maceio")));
			findRegistro.setTempoTotal(LocalTime.MIN.plusMinutes(tempoTotal.toMinutes()));
			findRegistro.setDataHoraSaida(LocalDateTime.now(ZoneId.of("America/Maceio")));
			return this.registroRepository.save(findRegistro);
		} else {
			throw new RegistroException(
					"O contrato com o nome " + contrato.getNomePaciente() + " não contem nenhum registro em aberto");
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

	/*
	 * Metodo busca todos registros de um contrato
	 * 
	 * @param numeroContrato String - numero do contratado cadastrado
	 * 
	 * @param page int - numero da pagina (Pageable)
	 * 
	 * @param size int - tamanho do pageable
	 * 
	 * @return Page<Registro>
	 */
	public Page<Registro> findAllRegistro(String numeroContrato, int page, int size) {
		List<Registro> registros = this.registroRepository.findAllRegistro(numeroContrato);
		for (Registro registro : registros) {
			registro.setSituacao(Situacao.valueOf(registro.getSituacao().toString()));
		}
		PageRequest pageable = PageRequest.of(page, size);
		return new PageImpl<Registro>(registros, pageable, registros.size());
	}

	/*
	 * Metodo que troca a situacao do registro para troca do profisional
	 * 
	 * @param situacaoRegistro String - situacao do registro
	 * 
	 * @param registroId Long - id do registro
	 * 
	 * @return Registro
	 * 
	 * @throws RegistroException - lanca a essecao quando nao encontrar nenhum
	 * registro com esse id
	 */
	public Registro exchangeOfContractStatus (String situacaoRegistro, Long registroId, String servico,
			Double valorSessao) {
		Registro registro = this.registroRepository.findById(registroId).orElseThrow(
				() -> new RegistroException("Nenhum registro com esse id: " + registroId + "foi encontrado"));
		Situacao situacao = Situacao.valueOf(situacaoRegistro);
		
		if(registro.getSituacao() == Situacao.ATENDIMENTO_NORMAL && registro.getDataHoraSaida() == null) {
			if (registro.getPlanoContratado().getTipoContrato().equals(TipoContrato.PLANO)
					|| registro.getPlanoContratado().getTipoContrato().equals(TipoContrato.PARTICULAR)) {

				if (Situacao.AUSENCIA_DO_PROFISSIONAL == situacao) {
					registro.getPlanoContratado().setValorTotal(
							registro.getPlanoContratado().getValorTotal() - registro.getPlanoContratado().getValorPlano());
				}

				else if (Situacao.TROCA_DE_SERVICO == situacao && valorSessao != null && valorSessao != 0.0) {
					Servico findServico = this.servicoService.findServico(servico);
						Double diferenca = registro.getPlanoContratado().getValorPlano() - valorSessao;
						registro.getPlanoContratado().setServico(findServico);
						if (diferenca > 0) {

							registro.getPlanoContratado()
									.setValorTotal(registro.getPlanoContratado().getValorTotal() - diferenca);
						} else {
							registro.getPlanoContratado()
									.setValorTotal(registro.getPlanoContratado().getValorTotal() + Math.abs(diferenca));
						}
				}
				else {
					throw new RegistroException("O valor da sessão esta com valor nulo");
				}
			}
			registro.getContrato().calcularValorTotal();
			registro.setSituacao(situacao);
		}
		else {
			throw new RegistroException("A situação do regitro é " + registro.getSituacao().getDescricao() + "e ja contém horário de saída" + 
		registro.getDataHoraSaida());
		}


		return this.registroRepository.save(registro);

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
