package com.pk.lwm2m.client.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.pk.lwm2m.client.model.Device;

public class EntityDAO {
	
	private static EntityManager em;

	/**
	 * Fetches device record by provided UUID
	 * @param uuid
	 * @return
	 */
	public Device getDeviceFromUUID(String uuid) {
	        
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Query query = em.createQuery("from Device d where d.uuid=:arg1");
		query.setParameter("arg1",uuid);
		List<Device> result = query.getResultList();
		
		Device device = null;
		if(result.size()>0){
			device = result.get(0);
		}
		em.getTransaction().commit();	        
		em.close();
	    
		return device;
	}
	
	/**
	 * Inserts the Device record after successful registration
	 * @param device
	 */
	public void insertRegisterdDevice(Device device){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		em.merge(device);
		em.getTransaction().commit();	        
		em.close();
		
	}
	
	public void removeDevice(String uuid){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Query query = em.createQuery("from Device d where d.uuid=:arg1");
		query.setParameter("arg1",uuid);
		List<Device> result = query.getResultList();
		
		Device device = null;
		if(result.size()>0){
			device = result.get(0);
		}
		
		em.remove(device);
		em.getTransaction().commit();	        
		em.close();
	}
	
	public boolean updateDevice(Device device){
		
		boolean result = false;
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Query query = em.createQuery("from Device d where d.uuid=:arg1");
		query.setParameter("arg1",device.getUuid());
		
		Device dev = (Device) query.getSingleResult();
		if(dev != null){
			dev.setPaymentPlan(device.getPaymentPlan());
			result = true;
		}
		em.getTransaction().commit();	        
		em.close();
		
		return result;
	}
	
	public void deregisterDevice(String uuid){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Query query = em.createQuery("from Device d where d.uuid=:arg1");
		query.setParameter("arg1",uuid);
		
		Device dev = (Device) query.getSingleResult();
		if(dev != null){
			dev.setRegistered(false);
		}
		em.getTransaction().commit();	        
		em.close();
		
	}
}
