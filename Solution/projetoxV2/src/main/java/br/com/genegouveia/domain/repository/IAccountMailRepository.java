package br.com.genegouveia.domain.repository;
import java.util.concurrent.Future;

import br.com.genegouveia.domain.entity.account.User;

/**
 *
 */
public interface IAccountMailRepository
{
    /*-------------------------------------------------------------------
     *                          BEHAVIORS
     *-------------------------------------------------------------------*/
    /**
     * @param user
     */
    Future<Void> sendNewUserAccount( User user );

    /**
     *
     */
    Future<Void> sendPasswordReset( User user );

    /**
     *
     */
    Future<Void> sendPasswordResetNotice( User user );
}