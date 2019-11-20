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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.projetox.entity.Contrato;
import com.br.projetox.entity.DiaConsulta;
import com.br.projetox.entity.DiasSemana;
import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.Servico;
import com.br.projetox.entity.TipoContrato;
import com.br.projetox.exception.ImportPlanilhaException;
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
	 * Método para a busca de todos os contratos com ordenacao
	 * 
	 * @param pageNum int - numero da pagina
	 * 
	 * @param pageSize int - total de contratos retornado por pagina
	 * 
	 * @param direction Direction - tipo de ordenacao ASC ou DESC
	 * 
	 * @param atributo String - atributo da entidade que sera referencia para
	 * ordenacao
	 * 
	 * @return Page<contrato>
	 */
	public Page<Contrato> findAll(int pageNum, int pageSize, Direction direction, String atributo) {
		Page<Contrato> page = null;
		PageRequest pageable = PageRequest.of(pageNum, pageSize,
				org.springframework.data.domain.Sort.by(direction, atributo));
		page = this.repository.findAll(pageable);
		page.forEach(data -> data.clearToList());
		return page;
	}

	public void saveContrato(Contrato contrato) {
		this.repository.save(contrato);
	}
	/*
	 * Método de busca que utiliza o campo numero , nomePaciente aonde pode ser
	 * utilizados em conjunto ou separados
	 * 
	 * @param numero String - numero do contrato cadastrado
	 * 
	 * @param nomePaciente String - nome do paciente contido no contrato
	 * 
	 * @param pageable PageRequet - variavel que contem especificacoes de retorno
	 * 
	 * @return Page<Contrato>
	 */
	@Transactional(readOnly = true)
	public Page<Contrato> findByFilters(String numero, String nomePaciente, PageRequest pageable) {
		return this.repository.findByFilters(numero, nomePaciente, pageable);
	}

	/*
	 * Método de busca que utiliza o campo numero , nomePaciente aonde pode ser
	 * utilizados em conjunto ou separados
	 * 
	 * @param numero String - numero do contrato cadastrado
	 * 
	 * @param nomePaciente String - nome do paciente contido no contrato
	 * 
	 * @param pageable PageRequet - variavel que contem especificacoes de retorno
	 * 
	 * @param ativo boolean - variavel que delimita a busca por estado do contrato
	 * ativo ou inativo
	 * 
	 * @return Page<Contrato>
	 */
	public Page<Contrato> findByFiltersParamActive(String numero, String nomePaciente, PageRequest pageable,
			Boolean ativo) {
		return this.repository.findByFiltersParamActive(numero, nomePaciente, pageable, ativo);

	}

	/*
	 * Método para busca de contratos atraves do numero do contrato cadastrado
	 * 
	 * @param numeroContrato String - numero do contrato salvo
	 * 
	 * @return contrato
	 * 
	 * @throws NotFoundException - retorna a excessao quando nao for encontrado
	 * nenhum contrato
	 */
	public Contrato findByContractNumber(String numeroContrato) throws NotFoundException {
		Optional<Contrato> contrato = this.repository.findByNumero(numeroContrato);
		return contrato.orElseThrow(() -> new NotFoundException(
				"Nenhum contrato encontrado com esse número de contrato:" + numeroContrato));

	}

	/*
	 * Método que recebe a planilha de dados e faz aas contagens das linhas e a
	 * percorre, suportando somente os formatos xlxs e xls
	 * 
	 * @param file FileTransfer - contem a planinhla de dados , tipo da variavel da
	 * biblioteca do DWR
	 * 
	 * @return HashMap<String, Integer>
	 */
	public HashMap<String, Integer> importPlanilhaContratos(FileTransfer file) throws Exception {
		List<Contrato> listContrato = new ArrayList<>();
		Workbook workbook = WorkbookFactory.create(file.getInputStream());
		Sheet sheet = workbook.getSheetAt(0);
		final int numeroLinhas = sheet.getPhysicalNumberOfRows();

		for (int i = 1; i < numeroLinhas; i++) {
			try {
				Row linha = sheet.getRow(i);
				if (!(linha == null || linha.getCell(0) == null || linha.getCell(0).toString().equals(""))) {
					listContrato.add(this.recoveryContractValues(linha));
				}
			} catch (Exception e) {
				throw new ImportPlanilhaException("Erro na linha " + i + "." + e);
			}
		}

		workbook.close();
		return this.saveAndUpdateCount(listContrato);
	}

	/*
	 * Método que recupera os valores do contrato
	 * 
	 * @param linha Row - recebe as linhas das planilha
	 * 
	 * @return Contrato
	 */
	private Contrato recoveryContractValues(Row linha) throws Exception {
		Contrato contrato = new Contrato();
		Double numeroContrato = Double.parseDouble(linha.getCell(NUM_CONTRATO).toString());
		contrato.setNumero(numeroContrato.intValue() + "");
		contrato.setNomePaciente(linha.getCell(NOME_PACIENTE).toString());

		return recoverContractedPlans(linha, contrato);

	}

	/*
	 * Método que recupera os valores dos planos contratados
	 * 
	 * @param linha Row - recebe as linhas da planilha
	 * 
	 * @param contrato Contrato - recebe o contrato para insercao do plano
	 * 
	 * @return Contrato
	 */
	private Contrato recoverContractedPlans(Row linha, Contrato contrato) {
		List<Servico> servicos = this.servicoService.findAll();
		int contator = 2;
		int atualizarNumeroCelula = 0;
		while (!(linha.getCell(contator) == null || linha.getCell(contator).toString().equals(""))) {
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

			this.checkDaysOfTheWeek(diasSemana).forEach(dias -> planoContratado.getDiaConsulta().add(dias));

			contator += MULTIPLICADOR_PROXIMO_SERVICO;
			atualizarNumeroCelula += 6;
			planoContratado.setContrato(contrato);
			contrato.getPlanoContratado().add(planoContratado);
		}
		contrato.calcularValorTotal();

		return contrato;
	}

	/*
	 * Método verifica os dias da semana vindo da planilha
	 * 
	 * @param diasSemana String[] - um array de String contendo os dias da semana
	 * informados
	 * 
	 * @return List<DiaConsulta>
	 * 
	 * @throws NullPointerException - retorna a excessao quando os valores do array
	 * nao atender nenhum else if com a verificacao
	 */
	public List<DiaConsulta> checkDaysOfTheWeek(String diasSemana[]) {
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

	/*
	 * Metodo que salva ou atualiza os contratos
	 * 
	 * @param contratos List<> - lista contendo os contratos
	 * 
	 * @return HashMap<string,integer> - contagem de contratos salvos e atualizados
	 */
	private HashMap<String, Integer> saveAndUpdateCount(List<Contrato> contratos) {
		int update = 0;
		int save = 0;
		Optional<Contrato> findContrato = null;
		for (int i = 0; i < contratos.size(); i++) {
			findContrato = this.repository.findByNumero(contratos.get(i).getNumero());
			if (findContrato.isPresent()) {
				findContrato.get().setNomePaciente(contratos.get(i).getNomePaciente());
				findContrato.get().calcularValorTotal();
				findContrato.get().setValorTotal(contratos.get(i).getValorTotal());
				checkContractedPlan(contratos, findContrato, i);
				this.repository.save(findContrato.get());
				update += 1;
			} else {
				this.repository.save(contratos.get(i));
				save += 1;
			}

		}
		HashMap<String, Integer> map = new HashMap<>();
		map.put("update", update);
		map.put("save", save);
		return map;
	}

	/*
	 * Metodo que verifica se existe um plano contratado ja cadastrado no contrato
	 * ,se exister e le ira fazer o update se nao ira salvar o novo plano
	 * 
	 * @param contratos List<Contratos> - contem os contratos a serem verificados
	 * 
	 * @param contrato Optional<Contrato> - contem o contrato especifico para fazer
	 * a busca do plano contratado
	 * 
	 * @param i int - contador do for
	 * 
	 * @return void
	 */
	private void checkContractedPlan(List<Contrato> contratos, Optional<Contrato> findContrato, int i) {
		for (PlanoContratado planoContratado : contratos.get(i).getPlanoContratado()) {
			Long servicoId = this.servicoService.findServico(planoContratado.getServico().getServico()).getId();
			PlanoContratado findPlanoContratado = this.planoContratadoService.findPlanoContratadoAtivo(servicoId,
					findContrato.get().getId(), planoContratado.getTipoContrato());
			if (findPlanoContratado != null) {
				findPlanoContratado.setDiaConsulta(planoContratado.getDiaConsulta());
				findPlanoContratado.setHorarioEntrada(planoContratado.getHorarioEntrada());
				findPlanoContratado.setHorarioSaida(planoContratado.getHorarioSaida());
				findPlanoContratado.setServico(planoContratado.getServico());
				findPlanoContratado.setSessao(planoContratado.getSessao());
				findPlanoContratado.setTipoContrato(planoContratado.getTipoContrato());
				findPlanoContratado.setValorTotal(planoContratado.getValorTotal());
				findPlanoContratado.calcularValorSessao();
				findContrato.get().getPlanoContratado().add(findPlanoContratado);
			} else {
				planoContratado.calcularValorSessao();
				planoContratado.setContrato(findContrato.get());
				findContrato.get().getPlanoContratado().add(planoContratado);
			}

		}
	}

}
