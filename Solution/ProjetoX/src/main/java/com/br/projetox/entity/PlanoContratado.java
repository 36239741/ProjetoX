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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
	private Double valorHora;
	private LocalTime horarioEntrada;
	private LocalTime horarioSaida;
	private Double valorTotal;

	
	@OneToMany(targetEntity = DiaConsulta.class, cascade = {CascadeType.PERSIST},fetch = FetchType.LAZY)
	private List<DiaConsulta> diaConsulta;

	@Enumerated(EnumType.ORDINAL)
	private TipoContrato tipoContrato;

	@ManyToOne(targetEntity = Servico.class,optional = false,fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private Servico servico;

	@OneToMany(targetEntity = Registro.class,mappedBy = "planoContratado",orphanRemoval = true,cascade = { CascadeType.REMOVE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	private List<Registro> registro;
	
	@ManyToOne(targetEntity = Contrato.class,optional = false,fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private Contrato contrato;


}
