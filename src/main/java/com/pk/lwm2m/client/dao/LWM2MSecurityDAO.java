package com.pk.lwm2m.client.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.pk.lwm2m.client.model.LWM2MSecurity;

public class LWM2MSecurityDAO {
	
	private static EntityManager em;
	
	public LWM2MSecurity saveBootstrapInformation(LWM2MSecurity security){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		em.persist(security);
		
		em.getTransaction().commit();	        
		em.close();
		
		return security;
	}
	
	public void removeSecurityInformation(int id){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		LWM2MSecurity security = em.find(LWM2MSecurity.class, id);
		em.remove(security);
		
		em.getTransaction().commit();	        
		em.close();
	}
	
	public LWM2MSecurity findSecurityById(int id){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		LWM2MSecurity security = em.find(LWM2MSecurity.class, id);
		em.getTransaction().commit();	        
		em.close();
		
		return security;
	}

}
