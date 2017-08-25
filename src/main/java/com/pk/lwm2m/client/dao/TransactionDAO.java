package com.pk.lwm2m.client.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.pk.lwm2m.client.model.Deposit;
import com.pk.lwm2m.client.model.Transaction;

public class TransactionDAO {
	
	private static EntityManager em;
	
	public Transaction saveTransaction(Transaction transaction){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		em.persist(transaction);
		
		em.getTransaction().commit();	        
		em.close();
		
		return transaction;
	}
	
	public List<Transaction> getAllTransactionForVendingMachine(long deviceId){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		String selectQuery = "from Transaction t WHERE t.device.id=:arg0";
		
		Query query = em.createQuery(selectQuery);
		query.setParameter("arg0", deviceId);
		
		List<Transaction> transaction =  query.getResultList();
		
		em.getTransaction().commit();	        
		em.close();
		
		return transaction;
	}

}
