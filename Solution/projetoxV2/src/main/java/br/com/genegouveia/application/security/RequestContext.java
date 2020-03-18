package br.com.genegouveia.application.security;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.genegouveia.domain.entity.account.User;

/**
 *
 */
public abstract class RequestContext
{
	/*-------------------------------------------------------------------
	 * 		 						BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 *
	 * @return
	 */
	public static Optional<User> currentUser()
	{
		return Optional.ofNullable( SecurityContextHolder.getContext().getAuthentication() )
				.map( auth -> (User) auth.getPrincipal() );
	}
}
