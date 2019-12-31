package com.br.projetox.hibernate.envers;

import java.util.Calendar;
import java.util.TimeZone;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CustomRevisionListener implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		RevisionClass userRevEntity = (RevisionClass) revisionEntity;
		userRevEntity.setUserName(authentication.getName());
		userRevEntity.setDate(Calendar.getInstance(TimeZone.getTimeZone("America/Maceio")).getTime());		
	}

}
