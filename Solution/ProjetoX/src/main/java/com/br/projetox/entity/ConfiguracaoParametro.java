package com.br.projetox.entity;

import java.io.Serializable;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

	private LocalTime tempoSessao;
	private LocalTime tempoToleranciaAtraso;
	private Double valorMinutoAdicional;
}
