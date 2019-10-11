package com.br.projetox.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity(name = "tbl_dia_Consulta")
@EqualsAndHashCode
public class DiaConsulta implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8626885819771227527L;
	@Id
	@GeneratedValue
	private long id;
	@Enumerated(EnumType.ORDINAL)
	private DiasSemana diasSemana;
}
