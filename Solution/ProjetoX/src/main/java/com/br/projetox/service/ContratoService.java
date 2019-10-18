package com.br.projetox.service;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
	private ContratoRepository repository;

	private static final Integer NUM_CONTRATO = 0;
	private static final Integer NOME_PACIENTE = 1;
	private static final Integer PLANO = 2;
	private static final Integer NUMERO_SESSOES = 3;
	private static final Integer VALOR_PLANO = 4;
	private static final Integer ENTRADA_PADRAO = 5;
	private static final Integer SAIDA_PADRAO = 6;
	private static final Integer DIAS_SEMANA = 7;
	private static final Integer MULTIPLICADOR_PROXIMO_SERVICO = 7;

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
		return page;
	}

	public Optional<Contrato> findByNumeroContrato(int numeroContrato) throws NotFoundException {
		Optional<Contrato> contrato = this.repository.findByNumero(numeroContrato);
		contrato.orElseThrow(
				() -> new NotFoundException("Nenhum contrato encontrado com esse numero:" + numeroContrato));
		return contrato;
	}

	public void importPlanilhaContratos() throws Exception {
		Workbook workbook = WorkbookFactory.create(new File("/home/henrique/Documentos/GitHub/ProjetoX/Docs/PlanilhaDeDados.xlsx"));
		Sheet sheet = workbook.getSheetAt(0);
		final int numeroLinhas = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < numeroLinhas; i++) {
			try {
				this.insertContratoFromXls(i, sheet);
			} catch (Exception e) {
				throw new Exception(e.getMessage() + " linha " + i);
			}
		}
		workbook.close();
	}

	private void insertContratoFromXls(int i, Sheet sheet) {
		Row linha = sheet.getRow(i);
		Contrato contrato = new Contrato();
		contrato.setNumero(linha.getCell(NUM_CONTRATO).toString());
		contrato.setNomePaciente(linha.getCell(NOME_PACIENTE).toString());

		List<Servico> servicos = this.servicoService.listServicos();

		/* Para cada contratado */
		int contator = 1;
		while (linha.getCell(contator) != null) {

			PlanoContratado planoContratado = new PlanoContratado();
			String[] planoServico = new String[2];
			planoServico = linha.getCell(PLANO).toString().trim().split("-");
			planoContratado.setTipoContrato(
					(planoServico[0].equals("Particular") ? TipoContrato.PARTICULAR : TipoContrato.PLANO));
			for (Servico servico : servicos) {
				if (servico.getServico().equals(planoServico[1].trim())) {
					planoContratado.setServico(servico);
					break;
				}
			}
			planoContratado.setSessao(Integer.parseInt(linha.getCell(NUMERO_SESSOES).toString()));
			planoContratado.setValorPlano(Double.parseDouble(linha.getCell(VALOR_PLANO).toString()));
			planoContratado.setHorarioEntrada(null); // linha.getCell(ENTRADA_PADRAO).toString
			planoContratado.setHorarioSaida(null);// linha.getCell(ENTRADA_SAIDA).toString
			String[] diasSemana = new String[7];
			diasSemana = linha.getCell(DIAS_SEMANA).toString().trim().split(",");
			for (i = 0; i < diasSemana.length; i++) {
				if (diasSemana[i] != null) {
					DiaConsulta diaConsulta = new DiaConsulta();
					if (diasSemana[i].equals("SEG")) {
						diaConsulta.setDiasSemana(DiasSemana.SEGUNDA);
					} else if (diasSemana[i].equals("TER")) {
						diaConsulta.setDiasSemana(DiasSemana.TERCA);
					} else if (diasSemana[i].equals("QUAR")) {
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
					planoContratado.getDiaConsulta().add(diaConsulta);
				}
			}
			contator = contator + MULTIPLICADOR_PROXIMO_SERVICO;
			contrato.getPlanoContratado().add(planoContratado);
		}
		this.repository.save(contrato);
	}
}
