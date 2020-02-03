package com.br.projetox.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Relatorio {
	private String numeroContrato;
	private String nomePaciente;
	private Double valorContratado;
	private Double valorExecutado;
	private Double diferenca;
}
