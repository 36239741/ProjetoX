package com.br.projetox.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity(name = "tbl_contrato")
public class Contrato {
	
	/*
	 * ATRIBUTOS
	 */	
	
	@Id
	@GeneratedValue
	@Column(name = "contrato_id")
	private long id;
	
	@NotNull
	@Column(unique = true)
	private String numero;
	
	@NotBlank
	@Column(length = 90)
	private String nomePaciente;
	
	@NotNull
	private Double valorTotal;
	private Byte[] biometria;
	
	
	@OneToMany(mappedBy = "contrato",targetEntity = PlanoContratado.class,cascade = {CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.LAZY)
	private List<PlanoContratado> planoContratado;
	
	
}
