package com.br.projetox.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity(name = "tbl_usuario")
public class Usuario implements Serializable {
	
	/*
	 * ATRIBUTOS
	 */	
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private long id;
	private String email;
	private String senha;
	
	@Enumerated(EnumType.ORDINAL)
	private AuthorityType authorityType;


}
