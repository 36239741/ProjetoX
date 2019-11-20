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
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

	/*
	 * Valor da sessão
	 */
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
	

	@OneToMany(targetEntity = DiaConsulta.class,cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE,
			CascadeType.REMOVE},fetch = FetchType.EAGER)
	private List<DiaConsulta>  diaConsulta = new ArrayList<DiaConsulta>();
	
	@Enumerated(EnumType.ORDINAL)
	private TipoContrato tipoContrato;

	@ManyToOne(targetEntity = Servico.class,optional = false,fetch = FetchType.EAGER)
	private Servico servico;
	
	@ManyToOne(targetEntity = Contrato.class,optional = true,fetch = FetchType.LAZY)
	private Contrato contrato;
	
	@NotNull
	private Boolean ativo = true;
	

	public void calcularValorSessao() {
		this.valorPlano = this.valorTotal / this.sessao;
	}
	
	
			
	


}
