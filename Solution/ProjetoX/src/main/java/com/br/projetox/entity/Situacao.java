package com.br.projetox.entity;

/*Enum que representa a situacao do registro do paciente*/

/*O operador do sistema poderá alterar a situação de um registro diário que estiver na situação "Atendimento normal" 
 * (enquanto não houver registro de saída) para "Troca de serviço", caso o proFissional que iria atender o paciente 
 * se ausentou, e o paciente preferiu pela troca pontual do serviço. Nesse caso, o operador deve indicar qual o serviço
 *  escolhido para a substituição pontual.
 *  
 *  O operador do sistema poderá alterar a situação de um registro diário que estiver na situação "Ausência do paciente" 
 *  para "Ausência do profissional".
*/

public enum Situacao {
	AUSENCIA_DO_PACIENTE("Ausência do paciente"), AUSENCIA_DO_PROFISSIONAL("Ausência do Profissional"),
	TROCA_DE_SERVICO("Troca de serviço"), ATENDIMENTO_NORMAL("Atendimento normal");
	private String mensagem;

	private Situacao(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getDescricao() {
		return this.mensagem;
	}

	public static final int SITUACAO_ATENDIMENTO_NORMAL = 3;
}
