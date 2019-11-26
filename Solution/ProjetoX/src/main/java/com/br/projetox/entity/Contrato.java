package com.br.projetox.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
	@OneToMany(targetEntity = PlanoContratado.class,cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE}, fetch = FetchType.LAZY,mappedBy = "contrato")
	private List<PlanoContratado> planoContratado = new ArrayList<PlanoContratado>();
	@NotNull
	private Boolean ativo = true;
	
	@Transient
	private TipoContrato tipoContratoTransient;

	
	public void calcularValorTotal() {
		this.valorTotal = 0.0;
		for(PlanoContratado plano: this.planoContratado) {
			this.valorTotal += plano.getValorTotal();
		}
	}
	
	public void clearToList() {
		this.planoContratado.clear();
	}
	
    
	public TipoContrato getTipoContratoTransient() {
		int contratoPlanoParticular = 0;
		int contratoPlano = 0;
		
		for(PlanoContratado planoContratao: this.planoContratado) {
			if(planoContratao.getAtivo().equals(true)) {
			if(planoContratao.getTipoContrato().equals(TipoContrato.PLANO)) {
				this.tipoContratoTransient = TipoContrato.PLANO;
				contratoPlano = 1;
			}
			else if(planoContratao.getTipoContrato().equals(TipoContrato.PARTICULAR)){
				this.tipoContratoTransient = TipoContrato.PARTICULAR;
				contratoPlanoParticular = 1;
			}
			}
		}
		if (contratoPlanoParticular == 1 && contratoPlano == 1) {
			this.tipoContratoTransient = TipoContrato.MISTO;
		}
		else if(contratoPlanoParticular == 0 && contratoPlano == 0) {
			this.tipoContratoTransient = TipoContrato.VAZIO;
		}
		
		return this.tipoContratoTransient;
	}

	
	
}
