	package com.br.projetox.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/*Entidade que representa os registros de entrada e saida dos planos.*/

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Registro extends AbstractEntity implements Serializable {
	
	/*
	 * ATRIBUTOS
	 */	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -616540646619945740L;
	
	/* horario de registro da entrada do paciente. */
	private LocalDateTime dataHoraEntrada;
	/* horario de registro de saida do paciente. */
	private LocalDateTime dataHoraSaida;
	/* Tempo total de permanencia na  consulta do paciente. */
	private LocalTime tempoTotal;
	/* Valor total do registro do paciente */
	private Double valorTotal;
	/*
	 * Situacao em que um registro pode se encontrar dependendo da ocasiao, 
	 * (troca feita pelo operador do sistema).
	 */
	@Enumerated(EnumType.ORDINAL)
	private Situacao situacao;
	
	/* Associacao com a Entidade contrato. */
	@ManyToOne(targetEntity = Contrato.class,fetch = FetchType.LAZY)
	private Contrato contrato;
	
	/* Associacao com a entidade planoContratado. */
	@ManyToOne(targetEntity = PlanoContratado.class,fetch = FetchType.EAGER,optional = false)
	private PlanoContratado planoContratado;
	

}
