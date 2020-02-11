package com.br.projetox.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.projetox.entity.Contrato;
import com.br.projetox.entity.DiaConsulta;
import com.br.projetox.entity.DiasSemana;
import com.br.projetox.entity.PlanoContratado;
import com.br.projetox.entity.Registro;
import com.br.projetox.entity.Servico;
import com.br.projetox.entity.TipoContrato;
import com.br.projetox.exception.DuplicatePlanoContratadoException;
import com.br.projetox.exception.MapPlanoContratadoException;
import com.br.projetox.repository.PlanoContratoRepository;

import javassist.NotFoundException;

@Service
@Transactional
public class PlanoContratadoService {

	@Autowired
	private ServicoService servicoService;

	@Autowired
	private ContratoService contratoService;

	@Autowired
	private PlanoContratoRepository planoContraRepository;
	
	@Autowired
	private RegistroService registroService;
	


	public PlanoContratado findById(Long id) throws NotFoundException {
		return this.planoContraRepository.findById(id).orElseThrow(() -> 
		new NotFoundException("Nenhum plano encontrado com esse id: " + id) );
	}
	
	public void deleteLogical(String planoContratadoId) {
		this.planoContraRepository.deleteLogical(Long.parseLong(planoContratadoId));
		PlanoContratado planoContratado = this.planoContraRepository.findById(Long.parseLong(planoContratadoId)).get();
		Double totalContrato = planoContratado.getContrato().getValorTotal() - planoContratado.getValorTotal();
		planoContratado.getContrato().setValorTotal(totalContrato);
		this.planoContraRepository.save(planoContratado);
	}

	/*
	 * Método pega todos planos contratados ativos , de um determinado contrato
	 * 
	 * @param contratoId String - pega o numero do contrato
	 * 
	 * @return List<PlanoContratado>
	 */
	public List<PlanoContratado> findAllPlanoContratadoByContratoId(String numeroContrato) {
		List<PlanoContratado> listPlanos = this.planoContraRepository.findByContratoId(numeroContrato);
		LocalDate data = LocalDate.now();
		for (PlanoContratado planos : listPlanos) {
			Collections.sort(planos.getDiaConsulta(), new Comparator<DiaConsulta>() {
				@Override
				public int compare(DiaConsulta o1, DiaConsulta o2) {
					return o1.getDiasSemana().compareTo(o2.getDiasSemana());

				}
			});
			planos.setSaldoMensal(this.saldoMensal(data.getYear(), data.getMonthValue(),planos.getId()));
		}
		return listPlanos;
	}

	/*
	 * Método que mapeia um objeto do tipo PlanoContratado
	 * 
	 * @param mapPlanoContratado Map<String,Object> - recebe valores para mapear o
	 * plano contratado e o numero de contrato que sera inserido o plano
	 * 
	 * @return PlanoContrato
	 * 
	 * @throws NotFoundException - e exibida quando nao foi encontrado nenhum
	 * contrato
	 */
	public PlanoContratado mapPlanoContratado(Map<String, Object> mapPlanoContratado) throws NotFoundException {
		try {
			PlanoContratado planoContratado = new PlanoContratado();
			planoContratado.setHorarioEntrada(LocalTime.parse(mapPlanoContratado.get("horarioEntrada").toString()));
			planoContratado.setHorarioSaida(LocalTime.parse(mapPlanoContratado.get("horarioSaida").toString()));
			planoContratado.setValorSessao(Double.parseDouble(mapPlanoContratado.get("valorPlano").toString()));
			List<String> list = (List<String>) mapPlanoContratado.get("diaConsulta");
			planoContratado.setSessao(Integer.parseInt(mapPlanoContratado.get("sessao").toString()));
			String[] diaConsulta = new String[7];
			diaConsulta = list.toArray(diaConsulta);

			List<DiaConsulta> listDiaConsulta = this.contratoService.checkDaysOfTheWeek(diaConsulta);
			planoContratado.setDiaConsulta(listDiaConsulta);

			Contrato contrato = this.contratoService
					.findByContractNumber(mapPlanoContratado.get("numeroContrato").toString());
			planoContratado.setContrato(contrato);
			if (mapPlanoContratado.get("tipoContrato").toString().toLowerCase().equals("plano")) {
				planoContratado.setTipoContrato(TipoContrato.PLANO);
			} else {
				planoContratado.setTipoContrato(TipoContrato.PARTICULAR);
			}
			Servico servico = this.servicoService.findServico(mapPlanoContratado.get("servico").toString());
			planoContratado.setServico(servico);
			planoContratado.setValorTotal(Double.parseDouble(mapPlanoContratado.get("valorTotal").toString()));
			return planoContratado;
		} catch (NullPointerException e) {
			throw new MapPlanoContratadoException("Map com campos nulos");
		}
	}

	/*
	 * Método que salva um plano contratado antes verifica o plano pelo tipo de
	 * contrato e o servico , depois atualiza o valor total do contrato
	 * 
	 * @param mapPlanoContratado Map<String, Object> - recebe valores para mapear o
	 * plano contratado e o numero de contrato que sera inserido o plano
	 * 
	 * @return void
	 */
	public void savePlanoContratato(Map<String, Object> mapPlanoContratado) throws NotFoundException {
		PlanoContratado savePlanoContratado = this.mapPlanoContratado(mapPlanoContratado);
		PlanoContratado findPlanoContratado = this.findPlanoContratadoAtivo(savePlanoContratado.getServico().getId(),
				savePlanoContratado.getContrato().getId(), savePlanoContratado.getTipoContrato());
		if (findPlanoContratado == null) {
			savePlanoContratado.calcularValorAtendimento();
			this.planoContraRepository.save(savePlanoContratado);
			Contrato contrato = this.contratoService
					.findByContractNumber(mapPlanoContratado.get("numeroContrato").toString());
			contrato.setValorTotal(contrato.getValorTotal() + savePlanoContratado.getValorTotal());
			this.contratoService.saveContrato(contrato);
		} else {
			throw new DuplicatePlanoContratadoException("Plano contratado duplicado");
		}
	}

	/*
	 * Método que faz o update do valor total do contrato
	 * 
	 * @param mapPlanoContratado Map<String, Object> - recebe as informacoes do
	 * plano contratado e dos contrato
	 * 
	 * @return void
	 */
	private void contractUpdateValor(Map<String, Object> mapPlanoContratado) throws NotFoundException {
		Double totalContral = 0.0;

		List<PlanoContratado> list = this.planoContraRepository
				.findByContratoId(mapPlanoContratado.get("numeroContrato").toString());
		for (PlanoContratado planoContratado : list) {
			totalContral += planoContratado.getValorTotal();
		}
		Contrato contrato = this.contratoService
				.findByContractNumber(mapPlanoContratado.get("numeroContrato").toString());
		contrato.setValorTotal(totalContral);
		this.contratoService.saveContrato(contrato);
	}

	/*
	 * Método que faz o update do plano contratado
	 * 
	 * @param mapPlanoContratado Map<String, Object> - recebe as informacoes do
	 * plano contratado e dos contrato
	 * 
	 * @throws NotFoundException - e exibida quando nao foi encontrado nenhum
	 * contrato
	 */
	public void updatePlanoContrato(Map<String, Object> mapPlanoContratado) throws NotFoundException {
		PlanoContratado planoContratado = this.mapPlanoContratado(mapPlanoContratado);
		planoContratado.setId(Long.parseLong(mapPlanoContratado.get("id").toString()));
		planoContratado.calcularValorAtendimento();
		planoContratado.calcularValorSessao();
		this.planoContraRepository.save(planoContratado);
		this.contractUpdateValor(mapPlanoContratado);
	}

	public List<PlanoContratado> findAll() {
		return this.planoContraRepository.findAllAtivo();
	}
	public Map<String,String> countSessionByPlan() throws NumberFormatException, NotFoundException {
		List<Map<String,String>> map =this.planoContraRepository.countSessionByPlan();
		Map<String, String> mapServico = new HashMap<>();;
		Servico servico = null;
		for(Map<String,String> maps: map) {
			servico = this.servicoService.findById(Long.parseLong(maps.get("id")));
			mapServico.put(servico.getServico(), maps.get("sessao"));
		}
		return mapServico;
	}

	/*
	 * Método que soma os totais dos planos contratatos ativos separando eles por
	 * planos tipo misto e plano
	 * 
	 * @return map HashMap<String,Double> - retorna os valores por chave valor
	 */
	public HashMap<String, Double> findTotalActiveContracts() {
		HashMap<String, Double> map = new HashMap<>();
		Double valorTotalPlanoParticular = 0.0;
		Double valorTotalPlano = 0.0;
		for (PlanoContratado planoContratado : this.planoContraRepository.findAll()) {
			if (planoContratado.getTipoContrato().equals(TipoContrato.PARTICULAR) && planoContratado.getAtivo() == true
					&& planoContratado.getContrato().getAtivo() == true) {
				valorTotalPlanoParticular += planoContratado.getValorTotal();
			} else if (planoContratado.getTipoContrato().equals(TipoContrato.PLANO)
					&& planoContratado.getAtivo() == true && planoContratado.getContrato().getAtivo() == true) {
				valorTotalPlano += planoContratado.getValorTotal();
			}

		}
		map.put("Particular", valorTotalPlanoParticular);
		map.put("Plano", valorTotalPlano);
		return map;
	}

	/*
	 * Método que busca um plano contratado por tipo e pelo servico
	 * 
	 * @param tipoContrato TipoContrato - parametro que mostra tipo do contrato
	 * 
	 * @param serviceId long - parametro que tem o id do servico
	 * 
	 * @return PlanoContratado
	 */
	public PlanoContratado findbyContractTypeAndServiceId(TipoContrato tipoContrato, long serviceId) {
		return this.planoContraRepository.findbyContractTypeAndServiceId(tipoContrato, serviceId);
	}

	/*
	 * Método que busca os plano contratado ativos de um determinado contrato
	 * 
	 * @param tipoContrato TipoContrato - parametro que mostra tipo do contrato
	 * 
	 * @param serviceId long - parametro que tem o id do servico
	 * 
	 * @param contrato id long - parametro que carrega o id do contrato
	 * 
	 * @return PlanoContratado
	 */
	@Transactional(readOnly = true)
	public PlanoContratado findPlanoContratadoAtivo(long servicoId, long contratoId, TipoContrato tipoContrato) {
		return this.planoContraRepository.findPlanoContratadoAtivoByContratoAndServicoAndTipoContrato(servicoId,
				contratoId, tipoContrato);
	}
	
	public Double saldoMensal(int ano,int mes,Long planoId) {
		
		LocalDate dataInicial = LocalDate.of(ano,mes,1);

		LocalDate ultimoDiaMes = dataInicial.withDayOfMonth(dataInicial.lengthOfMonth());
		LocalDate dataFinal = LocalDate.of(ano,mes,ultimoDiaMes.getDayOfMonth());
		
		Page<Registro> registros = this.registroService.findByDateAndPLanoId(dataInicial.toString(), dataFinal.toString(), planoId, 0, 32);
		
		Double valorExecutado = 0.0;
		
		for(Registro registro : registros.getContent()) {
			if(registro.getValorTotal() != null) {
				valorExecutado += registro.getValorTotal();
			}
		}
		
		return valorExecutado;
	}
	


	/*
	 * Método que busca os planos contratados atraves da biometria cadastrada no
	 * contrato
	 * 
	 * @return List<PlanoContratado> - retorna todos os planosContratados
	 */
	public List<PlanoContratado> findByBiometria() throws NotFoundException, UnsupportedEncodingException {
		Contrato contrato = this.contratoService.findByBiometria();
		return this.planoContraRepository.findByContratoId(contrato.getNumero());
	}
	

	public List<PlanoContratado> findByDiaConsulta(DiasSemana diasSemana){
		return this.planoContraRepository.findByDiaConsulta(diasSemana);
	}
	
	

}
