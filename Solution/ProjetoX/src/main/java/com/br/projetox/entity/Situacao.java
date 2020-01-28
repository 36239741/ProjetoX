package com.br.projetox.entity;

public enum Situacao {
	AUSENCIA_DO_PACIENTE("Ausência do paciente"),
	AUSENCIA_DO_PROFISSIONAL("Ausência do Profissional"),
	TROCA_DE_SERVICO("Troca de serviço"),
	ATENDIMENTO_NORMAL("Atendimento normal");
	private String mensagem;
	
	private Situacao (String mensagem) {
		this.mensagem = mensagem;
	}
	
	public String getDescricao() {
		return this.mensagem;
	}
	
	public static final int SITUACAO_ATENDIMENTO_NORMAL = 3;
}
