package com.br.projetox.service;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.projetox.entity.Contrato;

@Service
@Transactional
public class HibernateEnversService {
	
	   
	   @Autowired
	   private EntityManagerFactory entityManager;
	
		
		public void findRevisionContrato() {
			AuditReader auditReader = AuditReaderFactory.get(this.entityManager.createEntityManager());
			List<Object> list = auditReader.createQuery().forRevisionsOfEntity(Contrato.class, false, true).getResultList();
			for(Object lists : list) {
				System.out.println(lists.toString());
			}
		}
}
