package br.com.genegouveia.domain.entity.contrato;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.eits.common.domain.entity.AbstractEntity;
import br.com.genegouveia.domain.entity.contrato.PlanoContratado;
import br.com.genegouveia.domain.entity.contrato.TipoContrato;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/*Entidade que representa os contratos de prestacoes de servicos do paciente.*/

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Contrato extends AbstractEntity implements Serializable {
	
	/*
	 * ATRIBUTOS
	 */	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4109793839195820893L;

	/* Numero do contrato do paciente. */
	@NotNull
	@Column(unique = true)
	private String numero;
	
	/* Nome do paciente (Crianca que frequenta a consulta). */
	@NotBlank
	@Column(length = 90)
	private String nomePaciente;
	
	/*
	 * Valor total contrado pelo responsavel do contrato. Soma de todos totais dos
	 * planos adquiridos.
	 */
	@NotNull
	private Double valorTotal;
	
	/* Biometria do paciente que frequenta as consultas. */
	private byte[] biometria;
	
	/* Valor do desconto especificado pelo admnistrador do sistema. */
	private Double desconto;
	
	/* Lista de planos Contratados */
	@JsonIgnoreProperties("contrato")
	@OneToMany(targetEntity = PlanoContratado.class,cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE}, fetch = FetchType.LAZY,mappedBy = "contrato")
	private List<PlanoContratado> planoContratado = new ArrayList<PlanoContratado>();
	
	/* Lista de registros */
	@JsonIgnoreProperties("contrato")
	@OneToMany(targetEntity = Registro.class,cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE}, fetch = FetchType.LAZY)
	private List<Registro> registro = new ArrayList<Registro>();
	
	/* Status do contrato. */
	@NotNull
	private Boolean ativo = true;
	
	/* Tipo que o contrato pode ser sendo eles plano ou particular. */
	@Transient
	private TipoContrato tipoContratoTransient;
	
	/* Um valor mensal calculado pela soma dos valores totais dos registros. */
	@Transient 
	private Double valorExecutado;
	
	/*
	 * Diferenca entre valor executado e valor contratado, para ter uma estimativa
	 * de lucro ou perda.
	 */
	@Transient 
	private Double diferenca;
	
	/* Metodo que faz o calculo do valor total do contrato */
	public void calcularValorTotal() {
		this.valorTotal = 0.0;
		for(PlanoContratado plano: this.planoContratado) {
			if(plano.getAtivo().equals(true)) {
				this.valorTotal += plano.getValorTotal();
			}
		}
	}

	/* Metodo que limpa a lista de plano contratado */
	public void limparListaPlanoContratado() {
		this.planoContratado.clear();
	}
	
	/* Metodo que limpa a lista de plano contratado */
	public void limparListaRegistro() {
		this.registro.clear();;
	}
	/* Metodo que identifica o tipo do contrato em Plano, Particular, Misto */
	public TipoContrato identificarTipoContrato() {
		int contratoPlanoParticular = 0;
		int contratoPlano = 0;
		
		for(PlanoContratado planoContratao: this.planoContratado) {
			if(planoContratao.getAtivo().equals(true)) {
			if(planoContratao.getTipoContrato().equals(TipoContrato.PLANO)) {
				this.tipoContratoTransient = TipoContrato.PLANO;
				contratoPlano = 1;
			}
			else if(planoContratao.getTipoContrato().equals(TipoContrato.PARTICULAR)){
				this.tipoContratoTransient = TipoContrato.PARTICULAR;
				contratoPlanoParticular = 1;
			}
			}
		}
		if (contratoPlanoParticular == 1 && contratoPlano == 1) {
			this.tipoContratoTransient = TipoContrato.MISTO;
		}
		else if(contratoPlanoParticular == 0 && contratoPlano == 0) {
			this.tipoContratoTransient = TipoContrato.VAZIO;
		}
		
		return this.tipoContratoTransient;
	}
	
	/* Metodo calcula o valor executado de um contrato */
	public void calcularValorExecutado() {
		this.valorExecutado = 0.0;
		if(!this.registro.isEmpty()) {
			for(Registro registros: this.registro) {
				if(registros.getValorTotal() != null) {
					this.valorExecutado += registros.getValorTotal();
				}
					
			}
		}

	}
	
	/* Metodo calcula a diferenca entre o valor total e o valor executado */
	public void calcularDiferenca() {
		this.diferenca = Math.abs(this.valorTotal - this.valorExecutado);
		
	}
	
}
