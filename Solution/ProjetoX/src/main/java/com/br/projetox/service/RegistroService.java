package com.br.projetox.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.file.ConfigurationSource.Resource;
import org.directwebremoting.io.FileTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.br.projetox.entity.ConfiguracaoParametro;
import com.br.projetox.entity.Contrato;
import com.br.projetox.entity.DiasSemana;
import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.Registro;
import com.br.projetox.entity.Servico;
import com.br.projetox.entity.Situacao;
import com.br.projetox.entity.TipoContrato;
import com.br.projetox.exception.RegistroException;
import com.br.projetox.infrastructure.scheduling.SupportScheduling;
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

	@Autowired
	private ServicoService servicoService;

	@Autowired
	private SupportScheduling supportScheduling;

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

		if (findRegistro == null || findRegistro.getDataHoraSaida() != null) {
			Registro registro = new Registro();
			registro.setContrato(contrato);
			registro.setPlanoContratado(planoContratado);
			registro.setValorTotal(planoContratado.getValorPlano());
			registro.setDataHoraEntrada(LocalDateTime.now(ZoneId.of("America/Maceio")));
			registro.setSituacao(Situacao.ATENDIMENTO_NORMAL);
			this.registroRepository.save(registro);
			this.supportScheduling.scheduleSaidaAutomatica(registro);
			return registro;

		} else {
			throw new RegistroException("O contrato com o nome " + contrato.getNomePaciente() + " tem um registro em "
					+ findRegistro.getDataHoraEntrada().format(DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm"))
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
		if (dataInicial.isEmpty() == false && dataFinal.isEmpty() == false && contratoId.isEmpty() == false) {
			Pageable pagebale = PageRequest.of(page, size);
			return this.registroRepository.findByDate(LocalDateTime.parse(dataInicial + "T00:00:00"),
					LocalDateTime.parse(dataFinal + "T00:00:00"), Long.parseLong(contratoId), pagebale);
		} else {
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
					findRegistro.getPlanoContratado().getHorarioSaida().plusMinutes(configParametro.getTempoToleranciaAtraso().getMinute()),
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
		PageRequest pageable = PageRequest.of(page, size);
		Page<Registro> registros = this.registroRepository.findAllRegistro(numeroContrato, pageable);

		for (Registro registro : registros.getContent()) {
			registro.setSituacao(Situacao.valueOf(registro.getSituacao().toString()));
		}

		return registros;
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
	public Registro exchangeOfContractStatus(String situacaoRegistro, Long registroId, String servico,
			Double valorSessao) {
		Registro registro = this.registroRepository.findById(registroId).orElseThrow(
				() -> new RegistroException("Nenhum registro com esse id: " + registroId + "foi encontrado."));
		Situacao situacao = Situacao.valueOf(situacaoRegistro);

		if (registro.getSituacao() == Situacao.ATENDIMENTO_NORMAL && registro.getDataHoraSaida() == null) {
			if (registro.getPlanoContratado().getTipoContrato().equals(TipoContrato.PLANO)
					|| registro.getPlanoContratado().getTipoContrato().equals(TipoContrato.PARTICULAR)) {

				if (Situacao.AUSENCIA_DO_PROFISSIONAL == situacao) {
					registro.getPlanoContratado().setValorTotal(registro.getPlanoContratado().getValorTotal()
							- registro.getPlanoContratado().getValorPlano());
					registro.setValorTotal(0D);
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
								.setValorTotal(registro.getPlanoContratado().getValorTotal() + diferenca);
					}
				} else {
					throw new RegistroException("O valor da sessão esta com valor nulo.");
				}
			}
			registro.setValorTotal(valorSessao);
			registro.getContrato().calcularValorTotal();
			registro.setSituacao(situacao);
		} else {
			throw new RegistroException("A situação do atendimento deve ser Atendimento Normal.");
		}

		return this.registroRepository.save(registro);

	}

	/**
	 * Quando chegar no horário de saída do atendimento que estiver na situação
	 * "Atendimento Normal" e sem horário de saída registrado, o sistema
	 * automaticamente registra a saída do paciente com horário previsto de saída
	 * (sessão cheia). O valor deve ser calculado de acordo com as regras: - É
	 * cobrado o valor do serviço prestado de acordo com o valor da sessão.
	 * 
	 * @param registro
	 */
	public void registrarSaidaAutomatica(Registro registro) {
		Assert.notNull(registro, "Registro não informado.");
		Assert.isNull(registro.getDataHoraSaida(), "Este registro já encontra-se fechado.");
		Assert.isTrue(registro.getSituacao().equals(Situacao.ATENDIMENTO_NORMAL),
				"A situação do atendimento deve ser Atendimento Normal.");
		registro.setValorTotal(registro.getPlanoContratado().getValorPlano());

		LocalDate dataAtual = LocalDate.now();
		LocalTime horaSaida = registro.getPlanoContratado().getHorarioSaida();
		LocalDateTime dataHoraSaida = dataAtual.atTime(horaSaida).atZone(ZoneId.systemDefault()).toLocalDateTime();
		Duration tempoTotal = Duration.between(registro.getDataHoraEntrada().toLocalTime(), dataHoraSaida);
		registro.setTempoTotal(LocalTime.MIN.plusMinutes(tempoTotal.toMinutes()));
		registro.setDataHoraSaida(dataHoraSaida);
		this.registroRepository.save(registro);
	}

	/**
	 * Quando passar 10 minutos do horário de início do atendimento sem registro de
	 * entrada através da biometria, o sistema registra automaticamente o diário,
	 * com a situação "Ausência do paciente". O horário de saída fica registrado com
	 * o horário fim da sessão (sessão cheia). O valor deve ser calculado de acordo
	 * com as regras: - Regras para descontos e acréscimos (Contrato - Plano): -
	 * Ausência do paciente: não concede desconto. O valor cobrado pelo atendimento
	 * é total. - Regras para descontos e acréscimos (Contrato - Particular): -
	 * Ausência do paciente: concede desconto. Não é cobrado o valor da consulta.
	 * 
	 * @throws NotFoundException
	 */
	public Registro registrarAusenciaPacienteAutomaticamente(long planoId) throws NotFoundException {
		Assert.notNull(planoId, "Registro automático de ausência do paciente CANCELADO: Plano não informado.");

		PlanoContratado plano = this.planoContratadoService.findById(planoId);
		Assert.notNull(plano, "Registro automático de ausência do paciente CANCELADO: Plano não encontrado.");

		Assert.isTrue(plano.getAtivo().equals(true),
				"Registro automático de ausência do paciente CANCELADO: Plano não ativo.");

		// busca se há um registro aberto para o atendimento
		Registro findRegistro = this.registroRepository.findByPlanoContratadoAndMaxId(plano.getId());

		// se não houver registro ou se o mesmo encontrar-se fechado, abre um novo
		// registrando ausência do paciente
		if (findRegistro == null || findRegistro.getDataHoraSaida() != null) {
			Registro registro = new Registro();
			registro.setContrato(plano.getContrato());
			registro.setPlanoContratado(plano);
			registro.setSituacao(Situacao.AUSENCIA_DO_PACIENTE);

			LocalDate dataAtual = LocalDate.now();
			LocalTime horaEntrada = plano.getHorarioEntrada();
			LocalTime horaSaida = plano.getHorarioSaida();
			LocalDateTime dataHoraEntrada = dataAtual.atTime(horaEntrada).atZone(ZoneId.systemDefault())
					.toLocalDateTime();
			LocalDateTime dataHoraSaida = dataAtual.atTime(horaSaida).atZone(ZoneId.systemDefault()).toLocalDateTime();
			registro.setDataHoraEntrada(dataHoraEntrada);
			registro.setDataHoraSaida(dataHoraSaida);

			if (plano.getTipoContrato().equals(TipoContrato.PLANO)) {
				registro.setValorTotal(plano.getValorPlano());
			} else {
				registro.setValorTotal(0.0);
			}

			return this.registroRepository.save(registro);

		} else {
			throw new RegistroException("Registro automático de ausência do paciente CANCELADO: O Serviço contratado "
					+ plano.getServico().getServico() + " tem um registro em "
					+ findRegistro.getDataHoraEntrada().format(DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm"))
					+ " que ainda não foi fechado");
		}

	}

	/*
	 * cria uma planilha contendo todos os registros do contrato se nehum nome for
	 * designado para a planilha seu nome default sera Registros-do-contrato-numero
	 * + Numero do Contrato
	 * 
	 * @param numeroContrato String
	 * 
	 * @param nomeArquivo String
	 * 
	 * @return
	 * 
	 * @throws RegistroException
	 */
	public ByteArrayOutputStream createPlanilhaRegistros(String numeroContrato) throws IOException {
		XSSFWorkbook workBook = new XSSFWorkbook();
		XSSFSheet sheet = workBook.createSheet("Registros");

		// criando o cabelho da planilha
		createHead(this.createCellStyleHead(workBook), sheet);

		List<Registro> registros = this.registroRepository.findAllRegistroList(numeroContrato);
		if ((registros.isEmpty()))
			throw new RegistroException("Não existe nenhum registro nesse contrato");

		Locale locale = new Locale("pt", "BR");
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

		// insere os dados na planilha
		int i = 1;
		for (Registro registro : registros) {
			XSSFRow row = sheet.createRow(i);
			row.setRowStyle(this.createCellStyleDefault(workBook));
			row.createCell(0).setCellValue(registro.getDataHoraEntrada().format(DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm")).toString());
			
			if (registro.getDataHoraSaida() != null) {
				row.createCell(1).setCellValue(
						registro.getDataHoraSaida().format(DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm")).toString());

			} else {
				row.createCell(1).setCellValue("");
			}
			row.createCell(2).setCellValue(Situacao.valueOf(registro.getSituacao().toString()).getDescricao());
			if(registro.getTempoTotal() != null) {
				row.createCell(3).setCellValue(registro.getTempoTotal().toString());

			}else {
				row.createCell(3).setCellValue("");
			}
			if(registro.getValorTotal() != null) {
				row.createCell(4).setCellValue(numberFormat.format(registro.getValorTotal()));

			}
			else {
				row.createCell(4).setCellValue("");
			}
			i++;
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		workBook.write(stream);
		workBook.close();
		stream.close();

		return stream;
	}

	private CellStyle createCellStyleHead(Workbook workBook) {
		Font font = workBook.createFont();
		font.setFontHeightInPoints((short) 16);
		font.setColor(IndexedColors.WHITE.getIndex());
		font.setBold(true);

		CellStyle style = workBook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFont(font);
		return style;
	}

	private CellStyle createCellStyleDefault(Workbook workBook) {
		Font font = workBook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setColor(IndexedColors.BLACK.getIndex());

		CellStyle style = workBook.createCellStyle();
		style.setFont(font);
		return style;
	}

	private Cell createHead(CellStyle style, XSSFSheet sheet) {
		Cell cell;
		Row cabecalho = sheet.createRow(0);
		cell = cabecalho.createCell(0);
		cell.setCellValue("Horário de entrada");
		cell.setCellStyle(style);
		cell = cabecalho.createCell(1);
		cell.setCellValue("Horário de saída");
		cell.setCellStyle(style);
		cell = cabecalho.createCell(2);
		cell.setCellValue("Situação do atendimento");
		cell.setCellStyle(style);
		cell = cabecalho.createCell(3);
		cell.setCellValue("Tempo total");
		cell.setCellStyle(style);
		cell = cabecalho.createCell(4);
		cell.setCellValue("Valor total");
		cell.setCellStyle(style);

		return cell;
	}

}
