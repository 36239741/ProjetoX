package com.br.projetox.entity;

import java.time.LocalTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbl_plano")
public class PlanoContratado {
	
	/*
	 * ATRIBUTOS
	 */	
	
	
	@Id
	@GeneratedValue
	@Column(name = "plano_id")
	private long id;
	@NotNull
	private Double valorPlano;
	@NotNull
	private LocalTime horarioEntrada;
	@NotNull()
	private LocalTime horarioSaida;
	@NotNull
	private Double valorTotal;
	@NotNull
	private int sessao;
	
	@OneToMany(targetEntity = DiaConsulta.class ,cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.LAZY)
	@JoinColumn(name = "plano_id")
	private List<DiaConsulta> diaConsulta;

	@Enumerated(EnumType.ORDINAL)
	private TipoContrato tipoContrato;

	@ManyToOne(targetEntity = Servico.class,optional = false,fetch = FetchType.LAZY)
	private Servico servico;
	
	@ManyToOne(targetEntity = Contrato.class,optional = false,fetch = FetchType.LAZY)
	private Contrato contrato;


}
