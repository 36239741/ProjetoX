package br.com.genegouveia.domain.entity;

import java.io.Serializable;

import org.hibernate.envers.RevisionType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import br.com.genegouveia.application.security.RequestContext;
import br.com.genegouveia.domain.entity.account.User;

/**
 *
 */
public class EntityTrackingRevisionListener implements org.hibernate.envers.EntityTrackingRevisionListener
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@Override
	public void newRevision( Object revisionEntity )
	{
		RequestContext.currentUser()
				.ifPresent( user -> ((Revision<?, ?>) revisionEntity).setUserId( user.getId() ) );
	}

	/*
	 * (non-Javadoc)
	 * @see org.hibernate.envers.EntityTrackingRevisionListener#entityChanged(java.lang.Class, java.lang.String, java.io.Serializable, org.hibernate.envers.RevisionType, java.lang.Object)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void entityChanged( Class entityClass, String entityName, Serializable entityId, RevisionType revisionType, Object revisionEntity )
	{
	}

	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/
}