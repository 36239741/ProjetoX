package com.br.projetox.entity;

import java.util.List;

import org.springframework.util.Assert;

import com.br.projetox.exception.ContratoException;

/*Enum que representa os dias da semana*/

public enum DiasSemana {
	SEGUNDA(1),
	TERCA(2),
	QUARTA(3),
	QUINTA(4),
	SEXTA(5),
	SABADO(6),
	DOMINGO(7);
	
	private int diaDaSemana;
	
	private DiasSemana(int daysOfWeek) {
		this.diaDaSemana = daysOfWeek;
	}
	
	public int consultarDiasSemana() {
		return diaDaSemana;
	}
	
	/* Metodo que recupera os dias da semana em ordinal */
	public static DiasSemana diasSemanaOrdinal(int diasSemanaOrdinal) {
		DiasSemana diasSemana = null;
		for(DiasSemana dias: values()) {
			if(dias.consultarDiasSemana() == diasSemanaOrdinal) {
				diasSemana = dias;
			}
		}
		return diasSemana;
	}
	
	public static DiasSemana criarListDiasSemana(String diasSemana) {
		DiasSemana diaSemana = null;
				if (diasSemana.equals("SEG")) {
					diaSemana = DiasSemana.SEGUNDA;
				} else if (diasSemana.equals("TER")) {
					diaSemana = DiasSemana.TERCA ;
				} else if (diasSemana.equals("QUA")) {
					diaSemana = DiasSemana.QUARTA;
				} else if (diasSemana.equals("QUI")) {
					diaSemana = DiasSemana.QUINTA;
				} else if (diasSemana.equals("SEX")) {
					diaSemana = DiasSemana.SEXTA;
				} else if (diasSemana.equals("SAB")) {
					diaSemana = DiasSemana.SABADO;
				}
				return diaSemana;
	}
}
