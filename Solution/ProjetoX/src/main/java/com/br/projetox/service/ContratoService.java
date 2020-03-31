package com.br.projetox.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.directwebremoting.io.FileTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.br.projetox.entity.Contrato;
import com.br.projetox.entity.DiaConsulta;
import com.br.projetox.entity.DiasSemana;
import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.Registro;
import com.br.projetox.entity.Servico;
import com.br.projetox.entity.TipoContrato;
import com.br.projetox.exception.ContratoException;
import com.br.projetox.exception.ImportPlanilhaException;
import com.br.projetox.repository.ContratoRepository;
import com.br.projetox.util.BiometriaUlil;

import CIDBio.ImageAndTemplate;
import CIDBio.Template;
import javassist.NotFoundException;

@Service
@Transactional
public class ContratoService {

	@Autowired
	private ServicoService servicoService;

	@Autowired
	private PlanoContratadoService planoContratadoService;

	@Autowired
	private ContratoRepository repository;

	@Autowired
	private BiometriaUlil biometria;
	
	@Autowired
	private RegistroService registroService;

	private static final Integer NUM_CONTRATO = 0;
	private static final Integer NOME_PACIENTE = 1;
	private static final Integer PLANO = 2;
	private static final Integer NUMERO_SESSOES = 3;
	private static final Integer VALOR_PLANO = 4;
	private static final Integer ENTRADA_PADRAO = 5;
	private static final Integer SAIDA_PADRAO = 6;
	private static final Integer DIAS_SEMANA = 7;
	private static final Integer MULTIPLICADOR_PROXIMO_SERVICO = 6;

	

	
	/*
	 * Método que consulta todos contratos, aonde podem ser filtrados por nomePaciente e numero.
	 * 
	 * @param pageRequest
	 * @param numero
	 * @param nomePaciente
	 * @ativo
	 * 
	 * @return Page<contrato>
	 */
	public Page<Contrato> consultarTodosContratos(String numero, String nomePaciente,Boolean ativo, PageRequest pageRequest) {
		Page<Contrato> page  = this.repository.consultarContratos(numero, nomePaciente,ativo,pageRequest);
				
		for(Contrato contrato: page.getContent()) {
			contrato.calcularValorExecutado();
			contrato.calcularDiferenca();
		}
		return page;
	}



	/*
	 * Método que consulta um contrato pelo numero
	 * 
	 * @param numeroContrato
	 * 
	 * @return contrato
	 * */
	public Contrato consultarContratoPorNumeroContrato(String numeroContrato) {
		Contrato contrato = this.repository.findByNumero(numeroContrato).orElseThrow(()
				-> new IllegalArgumentException("Nenhum contrato encontrado com esse número de contrato: " + numeroContrato));
			
		contrato.calcularValorExecutado();
		contrato.calcularDiferenca();
		
		return contrato;

	}

	/*
	 *	Metodo que faz a importacao de uma planilha xlsx que contem as informacoes dos contratos assim preenchendo as entidades
	 *	e posteriormente persiste no banco e retorna o numero de contratos novos e os contratos atualizados
	 * 
	 * @param file
	 * 
	 * @return HashMap<String, Integer>
	 */
	public HashMap<String, Integer> importarPlanilhaContratos(FileTransfer file) throws Exception {
		List<Contrato> listaContrato = new ArrayList<>();
		Workbook workbook = WorkbookFactory.create(file.getInputStream());
		Sheet sheet = workbook.getSheetAt(0);
		final int numeroLinhas = sheet.getPhysicalNumberOfRows();

		for (int i = 1; i < numeroLinhas; i++) {
			try {
				Row linha = sheet.getRow(i);
				if (!(linha == null || linha.getCell(0) == null || linha.getCell(0).toString().equals(""))) {
					//Recupera os valores da planilha
					Contrato contrato = new Contrato();
					Double numeroContrato = Double.parseDouble(linha.getCell(NUM_CONTRATO).toString());
					contrato.setNumero(numeroContrato.intValue() + "");
					contrato.setNomePaciente(linha.getCell(NOME_PACIENTE).toString());
					int contator = 2;
					int atualizarNumeroCelula = 0;
					while (!(linha.getCell(contator) == null || linha.getCell(contator).toString().equals(""))) {
						PlanoContratado planoContratado = new PlanoContratado();
						String[] planoServico = new String[2];
						planoServico = linha.getCell(PLANO + atualizarNumeroCelula).toString().trim().split("-");
						planoContratado.setTipoContrato(
								(planoServico[0].trim().equals("Particular") ? TipoContrato.PARTICULAR : TipoContrato.PLANO));
						Servico servico = this.servicoService.consultarServicosPorNomeServicoServicoId(null,(planoServico[1].trim()));
								planoContratado.setServico(servico);

						Double numeroSessoes = Double.parseDouble(linha.getCell(NUMERO_SESSOES + atualizarNumeroCelula).toString());
						planoContratado.setSessao(numeroSessoes.intValue());
						planoContratado
								.setValorTotal(Double.parseDouble(linha.getCell(VALOR_PLANO + atualizarNumeroCelula).toString()));
						planoContratado.calcularValorSessao();
						String[] horaEntrada = linha.getCell(ENTRADA_PADRAO + atualizarNumeroCelula).toString().trim().split(":");
						planoContratado.setHorarioEntrada(
								LocalTime.of(Integer.parseInt(horaEntrada[0]), Integer.parseInt(horaEntrada[1]))); //
						String[] horaSaida = linha.getCell(SAIDA_PADRAO + atualizarNumeroCelula).toString().trim().split(":");
						planoContratado
								.setHorarioSaida(LocalTime.of(Integer.parseInt(horaSaida[0]), Integer.parseInt(horaSaida[1])));// linha.getCell(ENTRADA_SAIDA).toString
						String[] diasSemana = new String[7];
						diasSemana = linha.getCell(DIAS_SEMANA + atualizarNumeroCelula).toString().trim().split(",");
						Assert.notNull(diasSemana[0], "O campo dias da semana não foi preenchido. ");
						
						for(String diaSemana: diasSemana) {
							DiaConsulta diaConsulta = new DiaConsulta();
							diaConsulta.setDiasSemana(DiasSemana.criarListDiasSemana(diaSemana));
							planoContratado.getDiaConsulta().add(diaConsulta);
						}
						
						contator += MULTIPLICADOR_PROXIMO_SERVICO;
						atualizarNumeroCelula += 6;
						planoContratado.calcularValorAtendimento();
						planoContratado.setContrato(contrato);
						contrato.getPlanoContratado().add(planoContratado);
					}
					contrato.calcularValorTotal();
					listaContrato.add(contrato);
				}
			} catch (Exception e) {
				throw new ImportPlanilhaException("Erro na linha " + i + "." + e);
			}
		}
		workbook.close();
	
		int atualizado = 0;
		int salvo = 0;
		Optional<Contrato> consultarContrato = null;
		for (int i = 0; i < listaContrato.size(); i++) {
			//Verifica se o contrato existe 
			consultarContrato = this.repository.findByNumero(listaContrato.get(i).getNumero());
			if (consultarContrato.isPresent()) {
				consultarContrato.get().setNomePaciente(listaContrato.get(i).getNomePaciente());
				consultarContrato.get().calcularValorTotal();
				consultarContrato.get().setValorTotal(listaContrato.get(i).getValorTotal());
				
				for(PlanoContratado plano : listaContrato.get(i).getPlanoContratado()) {
					Servico servico = this.servicoService.consultarServicosPorNomeServicoServicoId(null, (plano.getServico().getServico()));
					
					//Verifica se o planoContratado existe
					PlanoContratado planoContratado = this.planoContratadoService.
					consultarPlanoContratadoAtivoPorServiceIdContratoIdTipoContrato(servico.getId(),
							listaContrato.get(i).getNumero(), plano.getTipoContrato());
					
					if(planoContratado != null) {
						planoContratado.setDiaConsulta(plano.getDiaConsulta());
						planoContratado.setHorarioEntrada(plano.getHorarioEntrada());
						planoContratado.setHorarioSaida(plano.getHorarioSaida());
						planoContratado.setServico(plano.getServico());
						planoContratado.setSessao(plano.getSessao());
						planoContratado.setTipoContrato(plano.getTipoContrato());
						planoContratado.setValorTotal(plano.getValorTotal());
						planoContratado.setValorSessao(plano.getValorSessao());
						planoContratado.setValorAtendimento(plano.getValorAtendimento());
						this.planoContratadoService.atualizarPlanoContratado(planoContratado);
					}
				}
				listaContrato.get(i).calcularValorTotal();
				this.repository.save(consultarContrato.get());
				atualizado += 1;
			} else {
				this.repository.save(listaContrato.get(i));
				salvo += 1;
			}

		}
		HashMap<String, Integer> map = new HashMap<>();
		map.put("atualizado", atualizado);
		map.put("salvo", salvo);
		return map;
	}


	
	/*
	 * Metodo atribui um desconto ao contrato com, o valor e informado pelo usuario 
	 * 
	 * @Param numeroContrato - String
	 * 
	 * @Param desconto - Double
	 * 
	 * @return Contrato
	 */
	
	public Contrato atribuirDesconto(String numeroContrato, Double desconto) throws Exception {
		List<PlanoContratado> plano = this.planoContratadoService.consultarPlanosDisponiveisDoContrato(numeroContrato);

		Assert.isTrue(!plano.isEmpty(), "Esse contrato nao possui nenhum plano contratado.");
		Assert.notNull(desconto, "O valor de desconto é nulo");
		
				
				Double valorTotalContrato = 0.0;
			/* valor do desconto divido pelo total de planos contratados do contrato */
				Double valorDesconto = 0.0;
				valorDesconto = desconto / plano.size();
				
				for (PlanoContratado planos : plano) {
					planos.setValorTotal(planos.getValorTotal() - valorDesconto);
					valorTotalContrato += planos.getValorTotal();
				}
				Contrato contrato = this.consultarContratoPorNumeroContrato(numeroContrato);
				contrato.setDesconto(desconto);
				contrato.setValorTotal(valorTotalContrato);
				return this.repository.save(contrato);

		}


	/*
	 * Método que salva no banco o template da biometria em forma de bytes
	 * @param numeroContrato Numero do contrato, que sera cadastrado a biometria
	 * @param binary binário da biometria capturada
	 */
	public void salvarBiometria(String numeroContrato, byte[] binary) {
		this.repository.salvarBiometria(numeroContrato, binary);
	}

	
	/*
	 * Metodo busca um contrato pela digital do paciente
	 * 
	 * @Param String numeroContrato - numero do contrato a ser consultado
	 * 
	 * @return Boolean - retorna true se sucesso e false se falhar
	 */
	public Contrato consultarContratosPorBiometria() throws UnsupportedEncodingException {
		ImageAndTemplate imgAndTemplate = null;
		List<Contrato> listContrato = null;
		Contrato returnContrato = null;
		listContrato = this.repository.findAll();
		imgAndTemplate = this.biometria.capturarBiometria();
		for (Contrato contrato : listContrato) {
			if (contrato.getBiometria() != null) {
				Boolean match = this.biometria.verificarDigital(contrato.getBiometria(), imgAndTemplate);
				if (Boolean.TRUE.equals(match)) {
					returnContrato = contrato;
				} 
			}
		}
		Assert.notNull(returnContrato, "Nenhum contrato cadastrado com essa biometria.");
		return returnContrato;
	}
	

	/*
	 * Metodo consulta o relatorio mensal dos contratos pelo mes e ano indicado pelo usuario
	 * 
	 * @param mes
	 * @param ano
	 * 
	 * @return List<Contrato>
	 */
	public List<Contrato> consultarRelatorioMensal(int mes, int ano) {
		List<Contrato> contratos = this.repository.findAll();
		LocalDate dataInicial = LocalDate.of(ano,mes,1);
		LocalDate ultimoDiaMes = dataInicial.withDayOfMonth(dataInicial.lengthOfMonth());
		LocalDate dataFinal = LocalDate.of(ano, mes,ultimoDiaMes.getDayOfMonth());

		for(Contrato contrato: contratos) {
			Page<Registro> registrosMensais = this.registroService.
					consultarRegistrosContratoPelaData(dataInicial.toString(), dataFinal.toString(), contrato.getNumero(), 0, 32);
			contrato.getRegistro().clear();
			for(Registro registro: registrosMensais.getContent()) {
				contrato.getRegistro().add(registro);
			}
			contrato.calcularValorExecutado();
			contrato.calcularDiferenca();
		}
		return contratos;
	}
	
	
	/* Metodo exporta uma planilha xlsx do relatorio mensal correspondente ao mes e o ano indicado pelo usuario
	 * 
	 * @param mes
	 * @param ano
	 * 
	 * @return ByteArrayOutputStream
	 * */ 
	public ByteArrayOutputStream exportarPlanilhaRelatorio(int mes, int ano) throws IOException {
		XSSFWorkbook workBook = new XSSFWorkbook();
		XSSFSheet sheet = workBook.createSheet("Relatorios");

		// criando o cabelho da planilha
		criarCabecalho(this.estiloCelulaCabecalho(workBook), sheet);

		List<Contrato> contratos = this.consultarRelatorioMensal(ano, mes);

		Locale locale = new Locale("pt", "BR");
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

		// insere os dados na planilha
		
		int i = 1;
		for (Contrato contrato : contratos) {
			XSSFRow row = sheet.createRow(i);
			// style das celulas

			CellStyle style = this.estiloPadraoCelulas(workBook, contrato);
			
			row.createCell(0).setCellValue(contrato.getNumero());
			row.createCell(1).setCellValue(contrato.getNomePaciente());
			row.createCell(2).setCellValue(numberFormat.format(contrato.getValorTotal()));
			row.createCell(3).setCellValue(numberFormat.format(contrato.getValorExecutado()));
			row.createCell(4).setCellValue(numberFormat.format(contrato.getDiferenca()));
			
			for(int contador = 0; contador <= 4; contador++) {
				row.getCell(contador).setCellStyle(style);
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
	
	/* Cabecalho da planilha do relatorio mensal */
	private Cell criarCabecalho(CellStyle style, XSSFSheet sheet) {
		Cell cell;
		Row cabecalho = sheet.createRow(0);
		cell = cabecalho.createCell(0);
		cell.setCellValue("Nº do contrato");
		cell.setCellStyle(style);
		cell = cabecalho.createCell(1);
		cell.setCellValue("Nome do paciente");
		cell.setCellStyle(style);
		cell = cabecalho.createCell(2);
		cell.setCellValue("Valor contratado");
		cell.setCellStyle(style);
		cell = cabecalho.createCell(3);
		cell.setCellValue("Valor executado");
		cell.setCellStyle(style);
		cell = cabecalho.createCell(4);
		cell.setCellValue("Diferença");
		cell.setCellStyle(style);

		return cell;
	}
	
	
	/* Estilo de celula do cabecalho */
	private CellStyle estiloCelulaCabecalho(Workbook workBook) {
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
	
	/*
	 * Estilo de padrao das celulas e colori as celulas conforme o valor da
	 * diferenca 
	 * 
	 * ValorExecutado > ValorTotal Verde 
	 * 
	 * ValorExecutado < ValorTotal Vermelho 
	 * 
	 * ValorExecutado = ValorTotal Azul
	 * 
	 * @return CellStyle
	 */
	private CellStyle estiloPadraoCelulas(Workbook workBook, Contrato contrato) {
		Font font = workBook.createFont();
		font.setFontHeightInPoints((short) 14);
		font.setColor(IndexedColors.BLACK.getIndex());
		
		CellStyle style = workBook.createCellStyle();
		if(contrato.getValorExecutado() < contrato.getValorTotal()) {
			
			style.setFillForegroundColor(IndexedColors.RED1.getIndex());
		}
		else if (contrato.getValorExecutado() > contrato.getValorTotal()) {
			style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		}
		else {
			style.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
		}
		
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFont(font);
		return style;
	}
	
	/* Metodo cancela a captura do leitor 
	 * 
	 * @return void
	 * */
	public void cancelarCapturaBiometria() {
		this.biometria.cancelCapture();
	}

	/*
	 * Metodo que salva um contrato
	 * 
	 * @return Contrato
	 */
	public Contrato salvarContrato(Contrato contrato) {
		return this.repository.save(contrato);
		
	}
	
	/*
	 * Método que lista os contratos por filtros
	 * 
	 * @param numero
	 * @param nomePaciente
	 * @param ativo
	 * @param pageRequest
	 * @return
	 */
	public Page<Contrato> consultarContratosAtivos(String numero, String nomePaciente, PageRequest pageRequest){
		 return this.repository.consultarContratos(numero, nomePaciente, true, pageRequest);
	}
	

}