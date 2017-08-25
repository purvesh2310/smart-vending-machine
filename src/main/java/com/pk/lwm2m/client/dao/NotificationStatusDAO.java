package com.pk.lwm2m.client.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.pk.lwm2m.client.model.NotificationStatus;

public class NotificationStatusDAO {
	
	private static EntityManager em;
	
	public void insertNotificationStatus(NotificationStatus notificationStatus){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(notificationStatus);
		em.getTransaction().commit();	        
		em.close();
	}
	
	public boolean checkNotificationStatus(String objectId){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Query query = em.createQuery("from NotificationStatus ns where ns.objectId=:arg1");
		query.setParameter("arg1",objectId);
		List<NotificationStatus> result = query.getResultList();
		
		em.getTransaction().commit();	        
		em.close();
		
		NotificationStatus notificationStatus = null;
		
		if(result.size() > 0){
			notificationStatus = result.get(0);
			return notificationStatus.isNotifyStatus();
		} else {
			return false;
		}
	}
	
	public boolean updateNotificationStatus(String objectId, boolean status){
		
		boolean result = false;
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Query query = em.createQuery("from NotificationStatus ns where ns.objectId=:arg1");
		query.setParameter("arg1",objectId);
		
		NotificationStatus nsObj = (NotificationStatus)query.getSingleResult();
		
		if(nsObj != null){
			nsObj.setNotifyStatus(status);
			result = true;
		}
		
		em.getTransaction().commit();	        
		em.close();
		
		return result;
	}
	
	public boolean findByObjectId(String objectId){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Query query = em.createQuery("from NotificationStatus ns where ns.objectId=:arg1");
		query.setParameter("arg1",objectId);
		NotificationStatus nsObj = null;
		try{
			nsObj = (NotificationStatus)query.getSingleResult();
		}catch(NoResultException e){
			
		}
		em.getTransaction().commit();	        
		em.close();
		
		if(nsObj != null){
			return true;
		}else{
			return false;
		}
	}

}
