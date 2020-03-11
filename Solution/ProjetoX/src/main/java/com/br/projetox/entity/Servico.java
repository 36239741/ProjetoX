package com.br.projetox.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/*Entidade que representa os servicos oferecidos pela empresa*/

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Servico extends AbstractEntity implements Serializable {
	
	/*
	 * ATRIBUTOS
	 */	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6794652905640725604L;
	
	/* Nome de um servico ofertado */
	@NotNull
	@Column(unique = true)
	private String servico;
	
	/* Valor cobrado pelo servico */
	private Double valor;
}
