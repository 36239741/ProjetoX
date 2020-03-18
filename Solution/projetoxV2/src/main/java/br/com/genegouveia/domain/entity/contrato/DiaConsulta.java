package br.com.genegouveia.domain.entity.contrato;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import br.com.eits.common.domain.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/*Entidade que representa os dias da semana em que o paciente tem consulta.*/

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class DiaConsulta extends AbstractEntity implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8626885819771227527L;
	
	/* Dias da semana em que o paciente agendou suas consultas. */
	@Enumerated(EnumType.ORDINAL)
	private DiasSemana diasSemana;
}
