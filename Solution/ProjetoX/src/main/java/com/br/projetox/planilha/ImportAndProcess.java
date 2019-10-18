package com.br.projetox.planilha;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.br.projetox.entity.Contrato;
import com.br.projetox.entity.DiaConsulta;
import com.br.projetox.entity.DiasSemana;
import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.TipoContrato;
import com.br.projetox.service.ServicoService;

import lombok.Cleanup;



public class ImportAndProcess {
	@Autowired
	private ServicoService servicoService;
	
	
	
	public void read() throws IOException {
		List<Contrato> listContrato = new ArrayList<>();
		List<PlanoContratado> listPlanoContratado = new ArrayList<>();
		PlanoContratado planoContratado = new PlanoContratado();
		Contrato contrato = new Contrato();
		@Cleanup Workbook workobook = WorkbookFactory.create(new File("/home/henrique/Downloads/PlanilhaDeDados(3).xlsx"));
		Sheet sheet = workobook.getSheetAt(0);
		List<Row> rows =  toRowList(sheet.iterator());
		rows.remove(0);
		rows.forEach(row -> {
			String[] array = new String [1];
			String plano = null;
			String dias = null;
			List<Cell> cells  = this.toCellList(row.cellIterator());
			
			//contrato.setNumero(cells.get(0).getNumericCellValue());
			contrato.setNomePaciente(cells.get(1).getStringCellValue());
			plano = cells.get(2).getStringCellValue();
			array = plano.split("-");
			if(array[0].equals("Particular")) {
				planoContratado.setTipoContrato(TipoContrato.PARTICULAR);
			}
			else {
				planoContratado.setTipoContrato(TipoContrato.PLANO);
			}
			System.out.println(array[1]);
			planoContratado.setServico(this.servicoService.findServico(array[1]));
			planoContratado.setSessao((int)cells.get(3).getNumericCellValue());
			planoContratado.setValorPlano((Double)cells.get(4).getNumericCellValue());
			planoContratado.setHorarioEntrada(LocalTime.parse(cells.get(5).getStringCellValue()));
			planoContratado.setHorarioSaida(LocalTime.parse(cells.get(6).getStringCellValue()));
			dias = cells.get(7).getStringCellValue();
			planoContratado.setDiaConsulta(this.diasSemana(dias));
			listPlanoContratado.add(planoContratado);
			contrato.setPlanoContratado(listPlanoContratado);
			listContrato.add(contrato);
		});
		listContrato.forEach(cont ->{
			System.out.println(cont);
		});
	}
	
	public List<DiaConsulta> diasSemana(String dias) {
		String[] diasSplit = dias.split(",");
		DiaConsulta diaConsulta = new DiaConsulta();
		List<DiaConsulta> listDiasConsulta = new ArrayList<>();
		for(String string: diasSplit) {
			if(string.equals("SEG")) {
				diaConsulta.setDiasSemana(DiasSemana.SEGUNDA);
			}
			else if(string.equals("TER")) {
				diaConsulta.setDiasSemana(DiasSemana.TERCA);
			}
			else if(string.equals("QUAR")) {
				diaConsulta.setDiasSemana(DiasSemana.QUARTA);
			}
			else if(string.equals("QUI")) {
				diaConsulta.setDiasSemana(DiasSemana.QUINTA);
			}
			else if(string.equals("SEX")) {
				diaConsulta.setDiasSemana(DiasSemana.SEXTA);
			}
			else if(string.equals("SAB")) {
				diaConsulta.setDiasSemana(DiasSemana.SABADO);
			}
			else {
				 throw new  NullPointerException("Nenhum dia da Consulta informado");
			}
			listDiasConsulta.add(diaConsulta);
		}
		return listDiasConsulta;
		
	}
	
	private List<Row> toRowList(Iterator<Row> iterator){
		return IteratorUtils.toList(iterator);
	}
	public List<Cell> toCellList(Iterator<Cell> iterator){
		return IteratorUtils.toList(iterator);
	}
}
	

