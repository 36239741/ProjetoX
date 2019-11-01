package com.br.projetox.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Contrato extends AbstractEntity implements Serializable {
	
	/*
	 * ATRIBUTOS
	 */	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4109793839195820893L;

	
	@NotNull
	@Column(unique = true)
	private String numero;
	
	@NotBlank
	@Column(length = 90)
	private String nomePaciente;
	
	@NotNull
	private Double valorTotal;
	
	private Byte[] biometria;
	
	@JsonIgnoreProperties("contrato")
	@OneToMany(targetEntity = PlanoContratado.class,cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE}, fetch = FetchType.LAZY)
	private List<PlanoContratado> planoContratado = new ArrayList<PlanoContratado>();
	
	public void calcularValorTotal() {
		this.valorTotal = 0.0;
		for(PlanoContratado plano: this.planoContratado) {
			this.valorTotal += plano.getValorTotal();
		}
	}
	
	public void clearToList() {
		this.planoContratado.clear();
	}
	

	
	
}
