	package com.br.projetox.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity(name="tbl_registro")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
public class Registro {
	
	/*
	 * ATRIBUTOS
	 */	
	
	
	@Id
	@GeneratedValue
	@Column(name = "registro_id")
	private long id;
	private LocalDateTime dataHoraEntrada;
	private LocalDateTime dataHoraSaida;
	private LocalTime tempoTotal;
	private Double valorTotal;
	@Enumerated(EnumType.ORDINAL)
	private Situacao situacao;
	
	@ManyToOne(targetEntity = Contrato.class,cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.PERSIST},fetch = FetchType.LAZY)
	private Contrato contrato;
	
	@ManyToOne(targetEntity = PlanoContratado.class,cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REMOVE},fetch = FetchType.LAZY,optional = false)
	private PlanoContratado planoContratado;

	
}
