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
}
