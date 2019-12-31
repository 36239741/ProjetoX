package com.br.projetox.hibernate.envers;

import org.hibernate.envers.RevisionType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ContratoAudit extends RevisionClass { /**
	 * 
	 */
	private static final long serialVersionUID = -2007444959808235975L;
		

	private String numero;
	

	private String nomePaciente;
	
	
	private Double valorTotal;
	
	private byte[] biometria;
	
	private Double desconto;
	

	private Boolean ativo = true;
	
	
	private RevisionType revisionType;
	
}
