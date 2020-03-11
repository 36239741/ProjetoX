package com.br.projetox.entity;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/*Entidade que em forma de planos as consultas que o paciente ira realizar.*/

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PlanoContratado extends AbstractEntity implements Serializable{
	
	/*
	 * ATRIBUTOS
	 */	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2720110599255732525L;

	/* Valor pago por uma consulta. */
	@NotNull
	private Double valorSessao;
	
	/* Horario que o paciente ira realizar a consulta. */
	@NotNull
	private LocalTime horarioEntrada;
	
	/* Horario de termino da consulta. */
	@NotNull()
	private LocalTime horarioSaida;
	
	/* Valor total do plano contratado. */
	@NotNull
	private Double valorTotal;
	
	/* Numeros de sessoes que serao realizadas no dia da consulta. */
	@NotNull
	private int sessao;
	
	/* Associacao com a entidade diaConsulta. */
	@OneToMany(targetEntity = DiaConsulta.class,cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE,
			CascadeType.REMOVE},fetch = FetchType.EAGER)
	private List<DiaConsulta>  diaConsulta = new ArrayList<DiaConsulta>();
	
	/* Tipo do contrato sendo ele plano ou particular. */
	@Enumerated(EnumType.ORDINAL)
	private TipoContrato tipoContrato;

	@ManyToOne(targetEntity = Servico.class,optional = false,fetch = FetchType.EAGER)
	private Servico servico;
	
	@ManyToOne(targetEntity = Contrato.class,optional = true,fetch = FetchType.EAGER)
	private Contrato contrato;
	
	/* Status de um plano contratado. */
	@NotNull
	private Boolean ativo = true;
	
	/* Valor por atendimento calculado pelo valor da sessa * numeros de sessoes. */
	@NotNull
	private Double valorAtendimento;
	
	/* Valor que representa o gasto mensal que o paciente teve em um plano. */
	@Transient
	private Double saldoMensal;
	
	
	/* Metodo que calcular o valor da sessao. */
	public void calcularValorSessao() {
		this.valorSessao = this.valorTotal / this.sessao;
	}
	
	/* Metodo que calcula o valor da consulta realizada no dia. */
	public void calcularValorAtendimento() {
		this.valorAtendimento = this.valorSessao * this.sessao;
	}
	
			
	


}
