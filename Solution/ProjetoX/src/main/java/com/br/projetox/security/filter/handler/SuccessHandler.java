package com.br.projetox.security.filter.handler;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.br.projetox.entity.Usuario;
import com.br.projetox.repository.UsuarioRepository;
import com.br.projetox.security.RequestContext;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {
	
	/**
	 *
	 */
	private static final Logger LOG = Logger.getLogger( AuthenticationSuccessHandler.class.getName() );
	
	/*-------------------------------------------------------------------
	 * 		 					 ATTRIBUTES
	 *-------------------------------------------------------------------*/
	//Repositories
	/**
	 *
	 */
	private final UsuarioRepository usuarioRepository;

	private final ObjectMapper objectMapper;

	@Autowired
	public SuccessHandler( UsuarioRepository usuarioRepository, ObjectMapper objectMapper )
	{
		this.usuarioRepository = usuarioRepository;
		this.objectMapper = objectMapper;
	}

	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/

	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.AuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	public void onAuthenticationSuccess( HttpServletRequest request, HttpServletResponse response, Authentication authentication ) throws IOException, ServletException
	{
		try
		{
			final Usuario user = RequestContext.currentUser().map( Usuario::getId ).flatMap( this.usuarioRepository::findById ).orElseThrow( () -> new IllegalArgumentException( "Ocorreu um problema ao atualizar o ultimo login do usuário" ) );
			this.usuarioRepository.save( user );
			this.objectMapper.writeValue( response.getWriter(), user );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			LOG.severe( "Ocorreu um problema ao atualizar o ultimo login do usuário" );
		}
	}
	
}

