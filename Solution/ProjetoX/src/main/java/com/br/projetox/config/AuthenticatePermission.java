package com.br.projetox.config;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

public class AuthenticatePermission implements PermissionEvaluator {

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		if(authentication != null && targetDomainObject instanceof String) {
			if("hasAccess".equalsIgnoreCase(String.valueOf(targetDomainObject))) {
				boolean hasAccess = validateAccess(String.valueOf(permission));
				return hasAccess;
			}
			return false;
		}
		return false;
	}

	private boolean validateAccess(String permission) {
		if("READ".equalsIgnoreCase(permission)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		// TODO Auto-generated method stub
		return false;
	}

}
