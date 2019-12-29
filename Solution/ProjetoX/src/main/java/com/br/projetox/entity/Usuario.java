package com.br.projetox.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Data
public class Usuario extends AbstractEntity  implements UserDetails, Serializable {
	
	/*
	 * ATRIBUTOS
	 */	
	
	/**
	 * 
	 */

	private static final long serialVersionUID = 30474420772499602L;


	@NotBlank
	@Column(unique = true)
	private String email;
	
	@NotBlank
	private String senha;
	
	@Enumerated(EnumType.ORDINAL)
	private AuthorityType authorityType;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authority = AuthorityUtils.createAuthorityList(this.getAuthorityType().toString());
		return authority;
	}
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.senha;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public UsernamePasswordAuthenticationToken converter() {
		return new UsernamePasswordAuthenticationToken(email, senha);
	}
	@JsonProperty(access = Access.WRITE_ONLY)
	public Long getId() {
		return super.getId();
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	public String getEmail() {
		return email;
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	public String getSenha() {
		return senha;
	}

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
