package com.br.projetox.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
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

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbstractEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private LocalDateTime created;
	
	protected LocalDateTime updated;
	
	@ManyToOne(targetEntity = Usuario.class)
	private Usuario usuario;
	
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
	
	@PreUpdate()
	protected void refreshUpdated()
	{
		this.refreshCreated();
		this.setUpdated( LocalDateTime.now() );
	}
}
