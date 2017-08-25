package com.pk.lwm2m.client.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.pk.lwm2m.client.model.LWM2MServer;

public class LWM2MServerDAO {
	
	private static EntityManager em;
	
	public int saveServerInformation(LWM2MServer server){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		em.persist(server);
		
		em.getTransaction().commit();	        
		em.close();

		return server.getShortServerId();
	}
	
	public void removeServerInformation(int shortServerId){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		LWM2MServer server = em.find(LWM2MServer.class, shortServerId);
		em.remove(server);
		
		em.getTransaction().commit();	        
		em.close();
		
	}
}
