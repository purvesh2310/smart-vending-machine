package com.pk.lwm2m.client.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.pk.lwm2m.client.model.Deposit;

public class DepositDAO {
	
	private static EntityManager em;
	
	public void saveDeposit(Deposit deposit){
	
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		em.persist(deposit);
		
		em.getTransaction().commit();	        
		em.close();
	}
	
	public Deposit getDepositInformation(long transactionId){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		String selectQuery = "from Deposit d WHERE d.transaction.id=:arg0";
		
		Query query = em.createQuery(selectQuery);
		query.setParameter("arg0", transactionId);
		
		Deposit deposit = (Deposit) query.getSingleResult();
		
		em.getTransaction().commit();	        
		em.close();
		
		return deposit;
	}

}
