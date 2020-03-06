package com.br.projetox.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
	


	public PlanoContratado consultarPlanoContratadoPorId(Long id) throws NotFoundException {
		return this.planoContraRepository.findById(id).orElseThrow(() -> 
		new NotFoundException("Plano contratado não encontrado.") );
	}
	
	public void deleteLogico(String planoContratadoId) {
		this.planoContraRepository.deleteLogico(Long.parseLong(planoContratadoId));
		Optional<PlanoContratado> planoContratado = this.planoContraRepository.findById(Long.parseLong(planoContratadoId));
		
		Assert.isTrue(planoContratado.isPresent(), "Plano contratado não encontrado.");
		
			Double totalContrato = planoContratado.get().getContrato().getValorTotal() - planoContratado.get().getValorTotal();
			planoContratado.get().getContrato().setValorTotal(totalContrato);
			this.planoContraRepository.save(planoContratado.get());
		
		
		}
		

	/*
	 * Método pega todos planos contratados ativos , de um determinado contrato
	 * 
	 * @param contratoId String - pega o numero do contrato
	 * 
	 * @return List<PlanoContratado>
	 */
	public List<PlanoContratado> consultarPlanoContratadoAtivoPorContrato(String numeroContrato) {
		List<PlanoContratado> listPlanos = this.planoContraRepository.consultarPlanoContratadoPorNumeroContrato(numeroContrato);
		LocalDate data = LocalDate.now();
		for (PlanoContratado planos : listPlanos) {
			Collections.sort(planos.getDiaConsulta(), new Comparator<DiaConsulta>() {
				@Override
				public int compare(DiaConsulta o1, DiaConsulta o2) {
					return o1.getDiasSemana().compareTo(o2.getDiasSemana());

				}
			});
			planos.setSaldoMensal(this.consultarSaldoMensal(data.getYear(), data.getMonthValue(),planos.getId(),planos.getContrato().getId()));
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
	public PlanoContratado mapearPlanoContratado(Map<String, Object> mapPlanoContratado) throws NotFoundException {
		try {
			PlanoContratado planoContratado = new PlanoContratado();
			planoContratado.setHorarioEntrada(LocalTime.parse(mapPlanoContratado.get("horarioEntrada").toString()));
			planoContratado.setHorarioSaida(LocalTime.parse(mapPlanoContratado.get("horarioSaida").toString()));
			planoContratado.setValorSessao(Double.parseDouble(mapPlanoContratado.get("valorPlano").toString()));
			List<String> list = (List<String>) mapPlanoContratado.get("diaConsulta");
			planoContratado.setSessao(Integer.parseInt(mapPlanoContratado.get("sessao").toString()));
			String[] diaConsulta = new String[7];
			diaConsulta = list.toArray(diaConsulta);

			List<DiaConsulta> listDiaConsulta = this.contratoService.checarDiasSemana(diaConsulta);
			planoContratado.setDiaConsulta(listDiaConsulta);

			Contrato contrato = this.contratoService
					.consultarContrato(mapPlanoContratado.get("numeroContrato").toString());
			planoContratado.setContrato(contrato);
			if (mapPlanoContratado.get("tipoContrato").toString().toLowerCase().equals("plano")) {
				planoContratado.setTipoContrato(TipoContrato.PLANO);
			} else {
				planoContratado.setTipoContrato(TipoContrato.PARTICULAR);
			}
			Servico servico = this.servicoService.consultarServicoPeloNome(mapPlanoContratado.get("servico").toString());
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
	public void salvarPlanoContratado(Map<String, Object> mapPlanoContratado) throws NotFoundException {
		PlanoContratado savePlanoContratado = this.mapearPlanoContratado(mapPlanoContratado);
		PlanoContratado findPlanoContratado = this.consultarPlanoContratadoAtivoPorServiceIdContratoIdTipoContrato(savePlanoContratado.getServico().getId(),
				savePlanoContratado.getContrato().getId(), savePlanoContratado.getTipoContrato());
		
		Assert.isTrue(findPlanoContratado == null, "Existe um plano contratado cadastrado com as mesmas características.");
		
			savePlanoContratado.calcularValorAtendimento();
			this.planoContraRepository.save(savePlanoContratado);
			Contrato contrato = this.contratoService
					.consultarContrato(mapPlanoContratado.get("numeroContrato").toString());
			contrato.setValorTotal(contrato.getValorTotal() + savePlanoContratado.getValorTotal());
			this.contratoService.saveContrato(contrato);

	}

	/*
	 * Método que faz o update do valor total do contrato
	 * 
	 * @param mapPlanoContratado Map<String, Object> - recebe as informacoes do
	 * plano contratado e dos contrato
	 * 
	 * @return void
	 */
	private void updateValorTotalContrato(Map<String, Object> mapPlanoContratado) throws NotFoundException {
		Double totalContral = 0.0;

		List<PlanoContratado> list = this.planoContraRepository
				.consultarPlanoContratadoPorNumeroContrato(mapPlanoContratado.get("numeroContrato").toString());
		for (PlanoContratado planoContratado : list) {
			totalContral += planoContratado.getValorTotal();
		}
		Contrato contrato = this.contratoService
				.consultarContrato(mapPlanoContratado.get("numeroContrato").toString());
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
	public void atualizarPlanoContratado(Map<String, Object> mapPlanoContratado) throws NotFoundException {
		PlanoContratado planoContratado = this.mapearPlanoContratado(mapPlanoContratado);
		planoContratado.setId(Long.parseLong(mapPlanoContratado.get("id").toString()));
		planoContratado.calcularValorAtendimento();
		planoContratado.calcularValorSessao();
		this.planoContraRepository.save(planoContratado);
		this.updateValorTotalContrato(mapPlanoContratado);
	}

	public List<PlanoContratado> consultarPlanosContratados() {
		return this.planoContraRepository.consultarPlanosContratadosAtivos();
	}
	

	/*
	 * Método que soma os totais dos planos contratatos ativos separando eles por
	 * planos tipo misto e plano
	 * 
	 * @return map HashMap<String,Double> - retorna os valores por chave valor
	 */
	public HashMap<String, Double> consultarSomaTotalPlanosAtivos() {
		HashMap<String, Double> map = new HashMap<>();
		Double valorTotalPlanoParticular = 0.0;
		Double valorTotalPlano = 0.0;
		
		for (PlanoContratado planoContratado : this.planoContraRepository.findAll()) {
			if (planoContratado.getTipoContrato().equals(TipoContrato.PARTICULAR) && planoContratado.getAtivo().equals(true)
					&& planoContratado.getContrato().getAtivo().equals(true)) {
				valorTotalPlanoParticular += planoContratado.getValorTotal();
			} else if (planoContratado.getTipoContrato().equals(TipoContrato.PLANO)
					&& planoContratado.getAtivo().equals(true) && planoContratado.getContrato().getAtivo().equals(true)) {
				valorTotalPlano += planoContratado.getValorTotal();
			}
		}
		Double somaParticularPlano = 0.0;
		somaParticularPlano = valorTotalPlanoParticular + valorTotalPlano;
		map.put("particular", valorTotalPlanoParticular);
		map.put("plano", valorTotalPlano);
		map.put("total", somaParticularPlano);
		return map;
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
	public PlanoContratado consultarPlanoContratadoAtivoPorServiceIdContratoIdTipoContrato(long servicoId, long contratoId, TipoContrato tipoContrato) {
		return this.planoContraRepository.consultarPlanoContratadoAtivoPorServiceIdContratoIdTipoContrato(servicoId,
				contratoId, tipoContrato);
	}
	
	public Double consultarSaldoMensal(int ano,int mes,Long planoId, Long contratoId) {
		
		LocalDate dataInicial = LocalDate.of(ano,mes,1);

		LocalDate ultimoDiaMes = dataInicial.withDayOfMonth(dataInicial.lengthOfMonth());
		LocalDate dataFinal = LocalDate.of(ano,mes,ultimoDiaMes.getDayOfMonth());
		
		Page<Registro> registros = this.registroService.consultarRegistroContratoPelaDataEPlanoId(contratoId,dataInicial.toString(), dataFinal.toString(), planoId, 0, 32);
		
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
	public List<PlanoContratado> consultarContratoPorBiometria() throws NotFoundException, UnsupportedEncodingException {
		Contrato contrato = this.contratoService.consultarContratosPorBiometria();
		return this.planoContraRepository.consultarPlanoContratadoPorNumeroContrato(contrato.getNumero());
	}
	

	public List<PlanoContratado> consultarContratoPorDiasSemana(DiasSemana diasSemana){
		return this.planoContraRepository.consultarPlanoContratadoPorDiasSemana(diasSemana);
	}
	
	

}
