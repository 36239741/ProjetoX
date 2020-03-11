package com.br.projetox.entity;

import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;

import com.br.projetox.security.RequestContext;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*Entidade que representa uma auditoria das classes e fornece o id*/

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbstractEntity {
	
	/* Identificador unico da classe */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/* Data de criacao de um dado */
	@NotNull
	private LocalDateTime created;
	
	/* Data de uma atualizacao de um dado */
	protected LocalDateTime updated;
	
	/* Usuario quem criou o ou atualizou o dado */
	@ManyToOne(targetEntity = Usuario.class)
	private Usuario usuario;
	
	/* Metodo que recupera a hora e a data de criacao e o usuario */
	@PrePersist()
	protected void refreshCreated()
	{
		if ( this.getCreated() == null )
		{
			this.setCreated( LocalDateTime.now() );
		}
		if(this.getUsuario() == null) {
			this.usuario = RequestContext.currentUser().orElse(null);
		}
	}
	
	/* Metodo que recupera a hora e data de atualizacao do dado */
	@PreUpdate()
	protected void refreshUpdated()
	{
		this.refreshCreated();
		this.setUpdated( LocalDateTime.now() );
	}
}
