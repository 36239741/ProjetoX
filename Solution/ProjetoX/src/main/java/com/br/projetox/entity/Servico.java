package com.br.projetox.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity(name = "tbl_servico")
public class Servico {
	
	/*
	 * ATRIBUTOS
	 */	
	
	
	@Id
	@GeneratedValue
	@Column(name = "servico_id")
	private long id;
	private String servico;
	private Double valor;
}
