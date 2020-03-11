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
	
	@Autowired
	private ConfigParametroService configParametros;
	
	
	/*
	 * Método consulta um PlanoContratado pelo id
	 * 
	 * @param id
	 * 
	 * @return PlanoContratado
	 */
	public PlanoContratado consultarPlanoContratadoPorId(Long id) throws NotFoundException {
		return this.planoContraRepository.findById(id).orElseThrow(() -> 
		new NotFoundException("Plano contratado não encontrado.") );
	}
	
	
	/*
	 * Método muda o status do campo ativo do PlanoContratado para false
	 * 
	 * @param planoContratadoId
	 * 
	 * @return void
	 */
	public void removerPlanoContratado(long planoContratadoId) {
		this.planoContraRepository.removerPlanoContratado(planoContratadoId);
		Optional<PlanoContratado> planoContratado = this.planoContraRepository.findById(planoContratadoId);
		
		Assert.isTrue(planoContratado.isPresent(), "Plano contratado não encontrado.");
		
			this.planoContraRepository.save(planoContratado.get());

		}
		

	/*
	 * Método que consulta todos planos ativos de um determinado contrato,ordenando os dias da consulta e calculando o saldo mensal
	 * 
	 * @param numeroContrato
	 * 
	 * @return List<PlanoContratado>
	 */
	public List<PlanoContratado> consultarPlanosDisponiveisDoContrato(String numeroContrato) {
		
		List<PlanoContratado> listPlanos = this.planoContraRepository.consultarPlanoContratadoPorNumeroContrato(numeroContrato);
		
		LocalDate data = LocalDate.now();
		LocalDate dataInicial = LocalDate.of(data.getYear(),data.getMonthValue(),1);
		LocalDate ultimoDiaMes = dataInicial.withDayOfMonth(dataInicial.lengthOfMonth());
		LocalDate dataFinal = LocalDate.of(data.getYear(), data.getMonthValue(),ultimoDiaMes.getDayOfMonth());
		
		for (PlanoContratado planos : listPlanos) {
			Collections.sort(planos.getDiaConsulta(), new Comparator<DiaConsulta>() {
				@Override
				public int compare(DiaConsulta o1, DiaConsulta o2) {
					return o1.getDiasSemana().compareTo(o2.getDiasSemana());

				}
			});
			Page<Registro> registros = null;
			registros = this.registroService.consultarRegistroMensal(planos.getContrato().getId(),dataInicial.toString(),dataFinal.toString(),planos.getId(), 0, 32);
			
			Double valorExecutado = 0.0;
			
			for(Registro registro : registros.getContent()) {
				if(registro.getValorTotal() != null) {
					valorExecutado += registro.getValorTotal();
				}
			}
			planos.setSaldoMensal(valorExecutado);
			
		}
		return listPlanos;
	}



	/*
	 * Método salva os dados de um planoContratado, fazendo uma verificacao se ja existe um plano contratado
	 * com o mesmo servico e tipo de contrato
	 * 
	 * @planoContratado
	 * 
	 * @return PlanoContratado
	 */
	public PlanoContratado salvarPlanoContratado(PlanoContratado planoContratado) throws NotFoundException {
		
		Servico servico = this.servicoService.consultarServicoPeloNome(planoContratado.getServico().getServico());
				
		PlanoContratado plano = this.planoContraRepository.
				consultarPlanoContratadoAtivoPorServiceIdContratoIdTipoContrato(servico.getId(), planoContratado.getContrato().getNumero(),
						planoContratado.getTipoContrato());
		
		Assert.isNull(plano, "Já existe um plano cadastrado no contrato do paciente " + planoContratado.getContrato().getNomePaciente() +
			" contendo o serviço "+ planoContratado.getServico().getServico() + " com o plano do tipo " + planoContratado.getTipoContrato());
		
		planoContratado.calcularValorAtendimento();
		planoContratado.calcularValorSessao();
		
		return this.planoContraRepository.save(planoContratado);
	}
	
	/*
	 * Método atualiza os dados de um planoContratado
	 * 
	 * @planoContratado
	 * 
	 * @return PlanoContratado
	 */
	public PlanoContratado atualizarPlanoContratado(PlanoContratado planoContratado) throws NotFoundException {
	
		planoContratado.calcularValorAtendimento();
		planoContratado.calcularValorSessao();
		
		return this.planoContraRepository.save(planoContratado);
	}


	/*
	 * Método que calcula os valores totais dos planos contratados ativos do tipo plano e particular, trazendo tambem
	 * o soma total dos dois tipos. Calcula o total de minutos contratados por plano
	 * 
	 * @return map HashMap<String,Double>
	 */
	public HashMap<String, Double> visualizarValoresCompiladosDosPlanos() {
		HashMap<String, Double> map = new HashMap<>();
		Double valorTotalPlanoParticular = 0.0;
		Double valorTotalPlano = 0.0;
		List<Servico> servicos = this.servicoService.consultarTodosServicos();
		
		for(Servico servico: servicos) {
			map.put(servico.getServico(), 0.0);
		}
		
		for (PlanoContratado planoContratado : this.planoContraRepository.findAll()) {
			if (planoContratado.getTipoContrato().equals(TipoContrato.PARTICULAR) && planoContratado.getAtivo().equals(true)
					&& planoContratado.getContrato().getAtivo().equals(true)) {
				valorTotalPlanoParticular += planoContratado.getValorTotal();
			} else if (planoContratado.getTipoContrato().equals(TipoContrato.PLANO)
					&& planoContratado.getAtivo().equals(true) && planoContratado.getContrato().getAtivo().equals(true)) {
				valorTotalPlano += planoContratado.getValorTotal();
			}
		
			// faz o calculo de em minutos contratado por servico
		if(map.containsKey(planoContratado.getServico().getServico()) && planoContratado.getAtivo().TRUE.equals(true)) {
			map.put(planoContratado.getServico().getServico(), map.get(planoContratado.getServico().getServico()) + 
					planoContratado.getSessao() * this.configParametros.consultarConfiguracaoPorId(1L).getTempoSessao().getMinute());
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
	public PlanoContratado consultarPlanoContratadoAtivoPorServiceIdContratoIdTipoContrato(long servicoId, String numeroContrato, TipoContrato tipoContrato) {
		return this.planoContraRepository.consultarPlanoContratadoAtivoPorServiceIdContratoIdTipoContrato(servicoId,
				numeroContrato, tipoContrato);
	}
	
	
	/*
	 * Metodo que faz a consulta do saldo mensal de um determinado contrato atraves
	 * do mes e do ano
	 * 
	 * @param ano
	 * 
	 * @param mes
	 * 
	 * @param planoId
	 * 
	 * @param contratoId
	 */
	public Double consultarSaldoMensal(int ano,int mes,Long planoId, Long contratoId) {
		
		LocalDate dataInicial = LocalDate.of(ano,mes,1);

		LocalDate ultimoDiaMes = dataInicial.withDayOfMonth(dataInicial.lengthOfMonth());
		LocalDate dataFinal = LocalDate.of(ano,mes,ultimoDiaMes.getDayOfMonth());
		
		Page<Registro> registros = this.registroService.consultarRegistroMensal(contratoId,dataInicial.toString(), dataFinal.toString(), planoId, 0, 32);
		
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
