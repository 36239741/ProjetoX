package com.br.projetox.service;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.projetox.entity.Contrato;
import com.br.projetox.hibernate.envers.ContratoAudit;
import com.br.projetox.hibernate.envers.RevisionClass;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class HibernateEnversService {
	
	   
	   @Autowired
	   private EntityManagerFactory entityManager;
	
		
		public void findRevisionContrato() {
			AuditReader auditReader = AuditReaderFactory.get(this.entityManager.createEntityManager());
			
			AuditQuery query = auditReader.createQuery().forRevisionsOfEntity(Contrato.class,false, true);
			
			@SuppressWarnings("unchecked")
			List<Object[]> result = query.getResultList();
			
			for(Object[] results : result) {
				Contrato contrato = (Contrato) results[0];
				RevisionClass revisionClass = (RevisionClass) results[1];
				RevisionType revisionType = (RevisionType) results [2];
				
				ObjectMapper objectMapper = new ObjectMapper();
				
				ContratoAudit contratoAudit = objectMapper.convertValue(contrato, ContratoAudit.class);
				
				contratoAudit.setDate(revisionClass.getDate());
				contratoAudit.setUserName(revisionClass.getUserName());
				contratoAudit.setRevisionType(revisionType);
				System.out.print(contratoAudit);
			}
		}
}
