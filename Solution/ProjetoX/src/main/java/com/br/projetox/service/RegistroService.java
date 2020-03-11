package com.br.projetox.service;

import java.io.IOException;
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
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.br.projetox.entity.ConfiguracaoParametro;
import com.br.projetox.entity.Contrato;
import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.Registro;
import com.br.projetox.entity.Situacao;
import com.br.projetox.entity.TipoContrato;
import com.br.projetox.exception.RegistroException;
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


	/*
	 * Metodo salva o horario de entrada do paciente, para registrar um horari de entrada o ultimo registro do contrato
	 * dever estar fechado
	 * 
	 * @oaram numeroContrato 
	 * 
	 * @param idPlanoContratado 
	 * 
	 * @return Registro
	 * 
	 * @throws NumberFormatException , NotFoundException, 
	 */
	public Registro salvarHorarioEntrada(String numeroContrato, long idPlanocontratado)
			throws NumberFormatException, NotFoundException {
		Contrato contrato = this.contratoService.consultarContratoPorNumeroContrato(numeroContrato);
		PlanoContratado planoContratado = this.planoContratadoService.consultarPlanoContratadoPorId(1L);
		
		Registro findRegistro = this.registroRepository.consultarUltimoRegistroContrato(contrato.getNumero());

		if(findRegistro != null) {
			
			Assert.notNull(findRegistro.getDataHoraSaida(), "O contrato com o nome " + contrato.getNomePaciente() + " tem um registro em "
					+ findRegistro.getDataHoraEntrada().format(DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm"))
					+ " que ainda não foi fechado.");
			
		}
		
			Registro registro = new Registro();
			registro.setContrato(contrato);
			registro.setPlanoContratado(planoContratado);
			registro.setValorTotal(planoContratado.getValorAtendimento());
			registro.setDataHoraEntrada(LocalDateTime.now(ZoneId.of("America/Maceio")));
			registro.setSituacao(Situacao.ATENDIMENTO_NORMAL);
			this.registroRepository.save(registro);
			return registro;


	}

	/*
	 * Metodo que busca os registros pela data inicial e final que se encontra no horario de entrada de um registro cadastrado 
	 * 
	 * @Param dataInicial String - data de inicio para busca
	 * 
	 * @Param dataFinal String - data final da busca
	 * 
	 * @Param numeroContrato String - numero do contrato
	 * 
	 * @Param page int - numero da pagina de retorno
	 * 
	 * @Param size int - tamanho da pagina de retorno
	 * 
	 * @return page<Registro>
	 */
	public Page<Registro> consultarRegistrosContratoPelaData(String dataInicial, String dataFinal, String numeroContrato, int page, int size) {
		Assert.isTrue(!dataInicial.isEmpty(), "Preencha o valor do campo data inicial.");
		Assert.isTrue(!dataFinal.isEmpty(), "Preencha o valor do campo data final.");
		Assert.isTrue(!numeroContrato.isEmpty(), "Valor do parâmetro numeroContrato está vazio. ");
		
			Pageable pagebale = PageRequest.of(page, size);
			return this.registroRepository.consultarRegistroPorDataInicialFinalNumeroContrato(LocalDateTime.parse(dataInicial + "T00:00:00"),
					LocalDateTime.parse(dataFinal + "T00:00:00"),numeroContrato, pagebale);

	}
	
	/* Metodo que consulta os registros de um determinado contrato e plano 
	 * @Param dataInicial String - data de inicio para busca
	 * 
	 * @Param dataFinal String - data final da busca
	 * 
	 * @Param contratoId Long - id do contrato
	 * 
 	 * @Param planoId Long - id de um plano
	 * 
	 * @Param page int - numero da pagina de retorno
	 * 
	 * @Param size int - tamanho da pagina de retorno
	 * 
	 * @return page<Registro>*/
	
	public Page<Registro> consultarRegistroMensal(Long contratoId,String dataInicial, String dataFinal, Long planoId, int page, int size) {
		Assert.isTrue(!dataInicial.isEmpty(), "Preencha o valor do campo data inicial.");
		Assert.isTrue(!dataFinal.isEmpty(), "Preencha o valor do campo data final.");
		Assert.isTrue(planoId > 0 , "Valor do parâmetro planoId é zero. ");
		Assert.isTrue(contratoId > 0, "Valor do parâmetro contratoId é zero. ");
		
			Pageable pagebale = PageRequest.of(page, size);
			return this.registroRepository.consultarRegistroPorDataInicialFinalPlanoIdContratoId(LocalDateTime.parse(dataInicial + "T00:00:00"),
					LocalDateTime.parse(dataFinal + "T00:00:00"),planoId,contratoId, pagebale);

	}

	/*
	 * Metodo que salva o horario de saida de um paciente e verifica se o horario de saida foi ultrapassado e se for o caso
	 * insere o valor de tolerancia ao valor total do registro, para efetuar o registro de saida, tem que ter um registro em aberto
	 * 
	 * @oaram numeroContrato String
	 * 
	 * @return Registro
	 * 
	 * @throws NotFoundException, FingerPrintException
	 */
	public Registro salvarHorarioSaida(String numeroContrato) throws NotFoundException {
		ConfiguracaoParametro configParametro = this.configParametrosService.consultarConfiguracaoPorId(1L);
		Registro consultarRegistro = this.registroRepository.consultarUltimoRegistroContrato(numeroContrato);
		
		Assert.notNull(consultarRegistro, "Não contêm nenhum registro no contrato de número " + numeroContrato + ".");
		Assert.isTrue(consultarRegistro.getDataHoraSaida() == null, "Não contêm nenhum registro de entrada em aberto para o paciente " + consultarRegistro.getContrato().getNomePaciente() + ".");
			
		// compara o horario de saida + tempo de tolerancia com o horario atual
			Duration verificaValorAdicional = Duration.between(
					LocalTime.now(ZoneId.of("America/Maceio")),
					consultarRegistro.getPlanoContratado().getHorarioSaida()
							.plusMinutes(configParametro.getTempoToleranciaAtraso().getMinute())
					);
		/*
		 * verifica se o tempo te tolerancia foi excedido, se for excedido adiciona o
		 * valor excedente se nao adiciona o valor do atendimento no valor total do registro
		 */
			Double valorAdicionalTolerancia = (verificaValorAdicional.toMinutes() > 0) ? consultarRegistro.getPlanoContratado().getValorAtendimento()
					+ (verificaValorAdicional.toMinutes() + configParametro.getTempoToleranciaAtraso().getMinute())
					* configParametro.getValorMinutoAdicional() : consultarRegistro.getPlanoContratado().getValorAtendimento();
			
			consultarRegistro.setValorTotal(valorAdicionalTolerancia);
			
			
			Duration tempoTotal = Duration.between(consultarRegistro.getDataHoraEntrada().toLocalTime(),
					LocalTime.now(ZoneId.of("America/Maceio")));
			
			consultarRegistro.setTempoTotal(LocalTime.MIN.plusMinutes(tempoTotal.toMinutes()));
			consultarRegistro.setDataHoraSaida(LocalDateTime.now(ZoneId.of("America/Maceio")));
			
			this.registroRepository.save(consultarRegistro);
		
		return consultarRegistro;

	}


	/*
	 * Metodo busca todos registros de um contrato pelo numeroContrato
	 * 
	 * @param numeroContrato String - numero do contratado cadastrado
	 * 
	 * @param page int - numero da pagina (Pageable)
	 * 
	 * @param size int - tamanho do pageable
	 * 
	 * @return Page<Registro>
	 */
	public Page<Registro> consultarTodosRegistrosDoContrato(String numeroContrato, int page, int size) {
		PageRequest pageable = PageRequest.of(page, size);
		Page<Registro> registros = this.registroRepository.consultarRegistros(numeroContrato, pageable);

		for (Registro registro : registros.getContent()) {
			registro.setSituacao(Situacao.valueOf(registro.getSituacao().toString()));
		}

		return registros;
	}

	/*
	 * O operador do sistema poderá alterar a situação de um registro diário que
	 * estiver na situação "Atendimento normal" (enquanto não houver registro de
	 * saída) para "Troca de serviço", caso o profissional que iria atender o
	 * paciente se ausentou, e o paciente preferiu pela troca pontual do serviço.
	 * Nesse caso, o operador deve indicar qual o serviço escolhido para a
	 * substituição pontual.
	 * 
	 * @param situacaoRegistro
	 * @param valorSessao
	 * 
	 * @return Registro
	 */
	public Registro registrarAlteracaoServico(Long registroId, Double valorSessao) {
		Registro registro = this.registroRepository.findById(registroId).orElseThrow(
				() -> new IllegalArgumentException("Nenhum registro com esse id: " + registroId + "foi encontrado."));
		
		Assert.isTrue(registro.getSituacao() == Situacao.ATENDIMENTO_NORMAL, "Para fazer a alteração do serviço, o registro deve se encontrar na situação de atendimento normal.");
		Assert.isNull(registro.getDataHoraSaida(), "Este registro já encontra-se fechado.");

		registro.setValorTotal(valorSessao * registro.getPlanoContratado().getSessao());
		registro.setSituacao(Situacao.TROCA_DE_SERVICO);
		return this.registroRepository.save(registro);

	}


	/*
	 * O operador dosistema poderá alterar a situação de um registro diário que
	 * estiver na situação "Ausência do paciente" para "Ausência do profissional".
	 * 
	 * 
	 * @param registroId
	 * 
	 * @return Registro
	 */

	public Registro registrarAusenciaDoProfisional(Long registroId) {
		Registro registro = this.registroRepository.findById(registroId).orElseThrow(
				() -> new IllegalArgumentException("Nenhum registro com esse id: " + registroId + "foi encontrado."));
		
		Assert.isTrue(registro.getSituacao().equals(Situacao.AUSENCIA_DO_PACIENTE), "Para declarar ausência do profissional o registro deve se encontrar em ausência do paciente.");
		
		Assert.isTrue(registro.getDataHoraSaida() != null , "Para declarar ausência do profissional o registro deve se encontrar fechado.");


			registro.getPlanoContratado().setValorTotal(
					registro.getPlanoContratado().getValorTotal() - registro.getPlanoContratado().getValorSessao());
			
			registro.setSituacao(Situacao.AUSENCIA_DO_PROFISSIONAL);
			registro.setValorTotal(0D);

		this.registroRepository.save(registro);
		return registro;
	}

	/**
	 * Todas as noites, para os registros abertos (sem horário de saída registrado) 
	 * e que estiverem na situação "Atendimento Normal", o sistema
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
		registro.setValorTotal(registro.getPlanoContratado().getValorSessao());

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

		PlanoContratado plano = this.planoContratadoService.consultarPlanoContratadoPorId(planoId);
		Assert.notNull(plano, "Registro automático de ausência do paciente CANCELADO: Plano não encontrado.");

		Assert.isTrue(plano.getAtivo().equals(true),
				"Registro automático de ausência do paciente CANCELADO: Plano não ativo.");

		// busca se há um registro aberto para o atendimento
		Registro findRegistro = this.registroRepository.consultarUltimoRegistroDoPlano(plano.getId());

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
				registro.setValorTotal(plano.getValorSessao());
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
	 * Metodo exporta uma planilha xlsx contendo todos registros de  um contrato
	 * 
	 * @param numeroContrato String
	 * 
	 * 
	 * @return ByteArrayOutputStream
	 * 
	 * @throws RegistroException
	 */
	public ByteArrayOutputStream exportarRegistrosEntradaSaida(String numeroContrato) throws IOException {
		XSSFWorkbook workBook = new XSSFWorkbook();
		XSSFSheet sheet = workBook.createSheet("Registros");

		// criando o cabelho da planilha
		criarCabecalho(this.criarEstiloDeEcelulaCabecalho(workBook), sheet);

		List<Registro> registros = this.registroRepository.consultarRegistros(numeroContrato, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
		
		if (registros.isEmpty())
			throw new RegistroException("Não existe nenhum registro nesse contrato");

		Locale locale = new Locale("pt", "BR");
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

		// insere os dados na planilha
		int i = 1;
		for (Registro registro : registros) {
			XSSFRow row = sheet.createRow(i);
			row.setRowStyle(this.criarEstiloCelulaPadrao(workBook));

			row.createCell(0).setCellValue(
					registro.getDataHoraEntrada().format(DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm")).toString());

			if (registro.getDataHoraSaida() != null) {
				row.createCell(1).setCellValue(
						registro.getDataHoraSaida().format(DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm")).toString());

			} else {
				row.createCell(1).setCellValue("");
			}
			row.createCell(2).setCellValue(Situacao.valueOf(registro.getSituacao().toString()).getDescricao());
			if (registro.getTempoTotal() != null) {
				row.createCell(3).setCellValue(registro.getTempoTotal().toString());

			} else {
				row.createCell(3).setCellValue("");
			}
			if (registro.getValorTotal() != null) {
				row.createCell(4).setCellValue(numberFormat.format(registro.getValorTotal()));

			} else {
				row.createCell(4).setCellValue("R$: " + 0);
			}
			i++;
		}
		for(int contador = 0; contador <= 4; contador++) {
			sheet.autoSizeColumn(contador);
			
		}
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		workBook.write(stream);
		workBook.close();
		stream.close();

		return stream;
	}
	
	/* Estilo das celulas do cabecalho */
	private CellStyle criarEstiloDeEcelulaCabecalho(Workbook workBook) {
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

	/* Estilo padrao das celulas */
	private CellStyle criarEstiloCelulaPadrao(Workbook workBook) {
		Font font = workBook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setColor(IndexedColors.BLACK.getIndex());

		CellStyle style = workBook.createCellStyle();
		style.setFont(font);
		return style;
	}
	
	/* Cabecalho da planilha dos registros */
	private Cell criarCabecalho(CellStyle style, XSSFSheet sheet) {
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
	
	/**
	 * Serviço que lista todos os registros abertos e na situação Atendimento normal
	 * @return
	 */
	public List<Registro> listRegistrosAbertos() {
		return this.registroRepository.consultarRegistrosAbertoAtendimentoNormal();
	}

}
