package br.com.genegouveia.domain.entity;

import java.io.Serializable;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.eits.common.domain.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/*Entidade que representa as configuracoes do sistema.
*/
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConfiguracaoParametro extends AbstractEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2372037262770701882L;
	/* tempo base de uma consulta do paciente.*/	
	private LocalTime tempoSessao;
	/* Tempo de tolerancia de atraso no horario de saida.*/
	private LocalTime tempoToleranciaAtraso;
	/* Valor do minuto adicionado ao exceder o tempo de tolerancia. */
	private Double valorMinutoAdicional;
}
