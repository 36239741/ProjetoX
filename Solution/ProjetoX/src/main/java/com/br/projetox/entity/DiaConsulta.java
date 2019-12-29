package com.br.projetox.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.envers.Audited;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Audited
@Entity
@EqualsAndHashCode(callSuper = true)
public class DiaConsulta extends AbstractEntity implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8626885819771227527L;

	@Enumerated(EnumType.ORDINAL)
	private DiasSemana diasSemana;
}
