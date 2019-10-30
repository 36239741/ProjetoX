package com.br.projetox.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.directwebremoting.io.FileTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.projetox.entity.Contrato;
import com.br.projetox.entity.DiaConsulta;
import com.br.projetox.entity.DiasSemana;
import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.Servico;
import com.br.projetox.entity.TipoContrato;
import com.br.projetox.repository.ContratoRepository;

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
	 * @Param int pageNum, int pageSize retorna todos contratos ne forma pageable
	 * 
	 * @Return Page<contrato>
	 */
	@Transactional(readOnly = true)
	public Page<Contrato> findAll(int pageNum, int pageSize) {
		Page<Contrato> page = null;
		PageRequest pageable = PageRequest.of(pageNum, pageSize);
		page = this.repository.findAll(pageable);
		page.forEach(data ->{
			data.clearToList();
		});
		return page;
	}
	@Transactional(readOnly = true)
	public Page<Contrato> findByFilters(String numero,String nomePaciente, PageRequest pageable){
		return this.repository.findByFilters(numero, nomePaciente, pageable);
	}

	public Contrato findByNumeroContrato(String numeroContrato) throws NotFoundException {
		Optional<Contrato> contrato = this.repository.findByNumero(numeroContrato);
		contrato.orElseThrow(
				() -> new NotFoundException("Nenhum contrato encontrado com esse número de contrato:" + numeroContrato));
		return contrato.get();
	}

	public HashMap<String, Integer> importPlanilhaContratos(FileTransfer file) throws Exception {
		List<Contrato> listContrato = new ArrayList<>();
		Workbook workbook = WorkbookFactory.create(file.getInputStream());
		Sheet sheet = workbook.getSheetAt(0);
		final int numeroLinhas = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < numeroLinhas; i++) {
			Row linha = sheet.getRow(i);
			if(!(linha == null ||  linha.getCell(0) == null || linha.getCell(0).toString().equals("") )) {
				listContrato.add(this.insertContratoFromXls(linha));
			}
		}
		workbook.close();
		return this.countSaveAndUpdateContract(listContrato);
	}

	private Contrato insertContratoFromXls(Row linha) throws NumberFormatException, NotFoundException {
		Contrato contrato = new Contrato();
		Double numeroContrato = Double.parseDouble(linha.getCell(NUM_CONTRATO).toString());
		contrato.setNumero(numeroContrato.intValue() + "");
		contrato.setNomePaciente(linha.getCell(NOME_PACIENTE).toString());

		List<Servico> servicos = this.servicoService.listServicos();

		/* Para cada contratado */
		int contator = 2;
		int atualizarNumeroCelula = 0;
		while (!(linha.getCell(contator) == null || linha.getCell(contator).toString().equals("") )) {

			PlanoContratado planoContratado = new PlanoContratado();
			String[] planoServico = new String[2];
			planoServico = linha.getCell(PLANO + atualizarNumeroCelula).toString().trim().split("-");
			planoContratado.setTipoContrato(
					(planoServico[0].trim().equals("Particular") ? TipoContrato.PARTICULAR : TipoContrato.PLANO));

			for (Servico servico : servicos) {
				if (servico.getServico().equals(planoServico[1].trim())) {
					planoContratado.setServico(servico);
					break;
				}
			}
			Double numeroSessoes = Double.parseDouble(linha.getCell(NUMERO_SESSOES + atualizarNumeroCelula).toString());
			planoContratado.setSessao(numeroSessoes.intValue());
			planoContratado.setValorTotal(Double.parseDouble(linha.getCell(VALOR_PLANO + atualizarNumeroCelula).toString()));
			planoContratado.calcularValorSessao();
			String[] horaEntrada = linha.getCell(ENTRADA_PADRAO + atualizarNumeroCelula).toString().trim().split(":");
			planoContratado.setHorarioEntrada(LocalTime.of(Integer.parseInt(horaEntrada[0]), Integer.parseInt(horaEntrada[1]))); // 
			String[] horaSaida = linha.getCell(SAIDA_PADRAO + atualizarNumeroCelula).toString().trim().split(":");
			planoContratado.setHorarioSaida(LocalTime.of(Integer.parseInt(horaSaida[0]), Integer.parseInt(horaSaida[1])));// linha.getCell(ENTRADA_SAIDA).toString
			String[] diasSemana = new String[7];
			diasSemana = linha.getCell(DIAS_SEMANA + atualizarNumeroCelula).toString().trim().split(",");
			
			this.listDiasSemanas(diasSemana).forEach(dias -> { planoContratado.getDiaConsulta().add(dias); });
			
			contator = contator + MULTIPLICADOR_PROXIMO_SERVICO;
			atualizarNumeroCelula +=  6;
			planoContratado.setContrato(contrato);
			contrato.getPlanoContratado().add(planoContratado);
		}
		contrato.calcularValorTotal();
		return contrato;
	}
	
	private List<DiaConsulta> listDiasSemanas(String diasSemana[]){
		List<DiaConsulta> listDiasConsulta = new ArrayList<>();
		for (int i = 0; i < diasSemana.length; i++) {
			if (diasSemana[i] != null) {
				DiaConsulta diaConsulta = new DiaConsulta();
				if (diasSemana[i].equals("SEG")) {
					diaConsulta.setDiasSemana(DiasSemana.SEGUNDA);
				} else if (diasSemana[i].equals("TER")) {
					diaConsulta.setDiasSemana(DiasSemana.TERCA);
				} else if (diasSemana[i].equals("QUA")) {
					diaConsulta.setDiasSemana(DiasSemana.QUARTA);
				} else if (diasSemana[i].equals("QUI")) {
					diaConsulta.setDiasSemana(DiasSemana.QUINTA);
				} else if (diasSemana[i].equals("SEX")) {
					diaConsulta.setDiasSemana(DiasSemana.SEXTA);
				} else if (diasSemana[i].equals("SAB")) {
					diaConsulta.setDiasSemana(DiasSemana.SABADO);
				} else {
					throw new NullPointerException("Nenhum dia da Consulta informado");
				}
				
				listDiasConsulta.add(diaConsulta);
			}
		}
		return listDiasConsulta;
	}
	
	private HashMap<String, Integer> countSaveAndUpdateContract(List<Contrato> contratos)  {
		int update = 0;
		int save = 0;
		Optional<Contrato> findContrato = null;
		for(int i =0 ; i < contratos.size(); i++) {
			findContrato = this.repository.findByNumero(contratos.get(i).getNumero());
			if(findContrato.isPresent()) {
				findContrato.get().setNomePaciente(contratos.get(i).getNomePaciente());
				findContrato.get().calcularValorTotal();
				findContrato.get().setValorTotal(contratos.get(i).getValorTotal());
				this.repository.save(findContrato.get());
				
				update += 1;
			}
			else {
				this.repository.save(contratos.get(i));
				save += 1;
			}
			
		}
		HashMap<String, Integer> map = new HashMap<>();
		map.put("update", update);
		map.put("save", save);
		return map;
	}
		
		
	
}
