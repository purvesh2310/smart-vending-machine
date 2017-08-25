package com.pk.lwm2m.client.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.pk.lwm2m.client.model.WriteAttribute;

public class DeviceManagementDAO {
	
	private static EntityManager em;
	
	public Object performReadOperation(String objectId, String objectInstanceId, String resourceId){

			EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
			em = emf.createEntityManager();
			em.getTransaction().begin();
			
			Query query = em.createQuery("select " + resourceId +" from " + objectId + " d where d.id=:arg1");
			query.setParameter("arg1",Long.parseLong(objectInstanceId));
			List<Object> result = query.getResultList();
			
			Object object = null;
			if(result.size()>0){
				object = result.get(0);
			}
			em.getTransaction().commit();	        
			em.close();
		
			return object;
	}
	
	public List<String> performDiscoverOperation(String objectId){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Query query = em.createNativeQuery("show columns from " + objectId);
		List<Object[]> result = query.getResultList();
		
		List<String> resourceName = new ArrayList<String>();
		
		for(Object[] obj : result){
			resourceName.add(obj[0].toString());
		}
		
		em.getTransaction().commit();	        
		em.close();
		
		return resourceName;
		
	}
	
	public int performWriteOperation(String objectId, String objectInstanceId, String resourceId, String value){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		String updateQuery = "UPDATE " + objectId + " SET " + resourceId + "=" + Integer.parseInt(value) + " where status_id=" + objectInstanceId;
		
		Query query = em.createNativeQuery(updateQuery);
		int success = query.executeUpdate();
		
		em.getTransaction().commit();	        
		em.close();
		
		return success;
		
	}
	
	public WriteAttribute performWriteAttributeOperation(WriteAttribute writeAtt){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Query query = em.createQuery("from WriteAttribute w where w.objectId=:arg1 and w.param=:arg2");
		query.setParameter("arg1",writeAtt.getObjectId());
		query.setParameter("arg2", writeAtt.getParam());
		WriteAttribute result = (WriteAttribute) query.getSingleResult();
		
		if(result != null){
			result.setValue(writeAtt.getValue());
		}else{
			em.persist(writeAtt);
		}
		
		em.getTransaction().commit();	        
		em.close();
		
		return writeAtt;
	}
	
	public List<WriteAttribute> findAttribute(String objectId){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Query query = em.createQuery("from WriteAttribute w where w.objectId=:arg1");
		query.setParameter("arg1",objectId);
		List<WriteAttribute> result = query.getResultList();

		em.getTransaction().commit();	        
		em.close();
		
		if(result.size()>0){
			return result;
		}else{
			return null;
		}
		
	}
	
	public int performShutDownExecuteOperation(String objectId, String objectInstanceId, String resourceId){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		String updateQuery = "UPDATE " + objectId + " SET " + resourceId + "=" + false + " where id=" + objectInstanceId;
		
		Query query = em.createNativeQuery(updateQuery);
		int result = query.executeUpdate();
		
		em.getTransaction().commit();	        
		em.close();
		
		return result;
	}
	
	public int performDeleteOperation(String objectId, String objectInstanceId){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		String deleteQuery = "DELETE FROM " + objectId + " WHERE status_id=" + objectInstanceId;
		Query query = em.createNativeQuery(deleteQuery);
		int result = query.executeUpdate();
		
		em.getTransaction().commit();	        
		em.close();
		
		return result;
	}

}
