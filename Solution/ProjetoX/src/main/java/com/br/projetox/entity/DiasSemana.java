package com.br.projetox.entity;

public enum DiasSemana {
	SEGUNDA(1),
	TERCA(2),
	QUARTA(3),
	QUINTA(4),
	SEXTA(5),
	SABADO(6),
	DOMINGO(7);
	
	private int daysOfWeek;
	
	private DiasSemana(int daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}
	
	public int getDiasSemana() {
		return daysOfWeek;
	}
	
	public static DiasSemana diasSemanaByOrdinal(int diasSemanaOrdinal) {
		DiasSemana diasSemana = null;
		for(DiasSemana dias: values()) {
			if(dias.getDiasSemana() == diasSemanaOrdinal) {
				diasSemana = dias;
			}
		}
		return diasSemana;
	}
}
