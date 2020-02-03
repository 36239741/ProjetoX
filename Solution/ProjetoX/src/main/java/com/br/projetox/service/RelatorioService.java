package com.br.projetox.service;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
import org.springframework.stereotype.Service;

import com.br.projetox.entity.Contrato;
import com.br.projetox.entity.Registro;
import com.br.projetox.entity.Relatorio;
import com.br.projetox.entity.Situacao;
import com.br.projetox.exception.RegistroException;

@Service
public class RelatorioService {
	@Autowired
	private RegistroService registroService;
	
	@Autowired
	private ContratoService contratoService;
	
	public Double valorExecutado(int ano,int mes, String numeroContrato) {
		
		LocalDate dataInicial = LocalDate.of(ano,mes,1);

		LocalDate ultimoDiaMes = dataInicial.withDayOfMonth(dataInicial.lengthOfMonth());
		LocalDate dataFinal = LocalDate.of(ano,mes,ultimoDiaMes.getDayOfMonth());
		
		List<Registro> registros = this.registroService.findByDate(dataInicial.toString(), dataFinal.toString(), numeroContrato, 0, 30).getContent();
		
		
		Double valorExecutado = 0.0;
		
		for(Registro registro : registros) {
			valorExecutado += registro.getValorTotal();
		}
		
		return valorExecutado;
		
	}
	

	
	public List<Relatorio> montarEntidadeRelatorio(int ano , int mes) {
		List<Contrato> contratos = this.contratoService.findAllContratos();
				
		List<Relatorio> relatorios = new ArrayList<>();
		for(Contrato contrato: contratos) {
			Relatorio relatorio = new Relatorio();
			relatorio.setNumeroContrato(contrato.getNumero());
			relatorio.setNomePaciente(contrato.getNomePaciente());
			relatorio.setValorContratado(contrato.getValorTotal());
			relatorio.setValorExecutado(this.valorExecutado(ano, mes, contrato.getNumero()));
			relatorio.setDiferenca( relatorio.getValorExecutado() - relatorio.getValorContratado() );
			relatorios.add(relatorio);
		}
		
		return relatorios;
	}
	
	
	public ByteArrayOutputStream createPlanilhaRelatorio(int ano, int mes) throws IOException {
		XSSFWorkbook workBook = new XSSFWorkbook();
		XSSFSheet sheet = workBook.createSheet("Relatorios");

		// criando o cabelho da planilha
		createHead(this.createCellStyleHead(workBook), sheet);

		List<Relatorio> relatorios = this.montarEntidadeRelatorio(ano, mes);

		Locale locale = new Locale("pt", "BR");
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

		// insere os dados na planilha
		
		int i = 1;
		for (Relatorio relatorio : relatorios) {
			XSSFRow row = sheet.createRow(i);
			// style das celulas

			CellStyle style = this.createCellStyleDefault(workBook, relatorio);
			
			row.createCell(0).setCellValue(relatorio.getNumeroContrato());
			row.createCell(1).setCellValue(relatorio.getNomePaciente());
			row.createCell(2).setCellValue(numberFormat.format(relatorio.getValorContratado()));
			row.createCell(3).setCellValue(numberFormat.format(relatorio.getValorExecutado()));
			row.createCell(4).setCellValue(numberFormat.format(relatorio.getDiferenca()));
			
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
	
	private Cell createHead(CellStyle style, XSSFSheet sheet) {
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
	
	private CellStyle createCellStyleDefault(Workbook workBook, Relatorio relatorio) {
		Font font = workBook.createFont();
		font.setFontHeightInPoints((short) 14);
		font.setColor(IndexedColors.BLACK.getIndex());

		CellStyle style = workBook.createCellStyle();
		
		if(relatorio.getDiferenca() < 0.0) {
			style.setFillForegroundColor(IndexedColors.RED1.getIndex());
		}
		else if (relatorio.getDiferenca() > 0.0) {
			style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
		}
		else {
			style.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
		}
		
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFont(font);
		return style;
	}
}
