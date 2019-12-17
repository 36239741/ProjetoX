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

	private LocalDateTime dataHoraEntrada;
	private LocalDateTime dataHoraSaida;
	private LocalTime tempoTotal;
	private Double valorTotal;
	@Enumerated(EnumType.ORDINAL)
	private Situacao situacao;
	
	@ManyToOne(targetEntity = Contrato.class,fetch = FetchType.LAZY)
	private Contrato contrato;
	
	@ManyToOne(targetEntity = PlanoContratado.class,fetch = FetchType.EAGER,optional = false)
	private PlanoContratado planoContratado;

	
}
