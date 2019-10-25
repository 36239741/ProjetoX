package com.br.projetox.entity;

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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity(name = "tbl_configuracao")
public class ConfiguracaoParametro {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "configuracao_id")
	private long id;
	private LocalTime tempoSessao;
	private LocalTime tempoToleranciaAtraso;
	private Double valorMinutoAdicional;
}
