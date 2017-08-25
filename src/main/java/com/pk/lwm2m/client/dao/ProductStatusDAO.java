package com.pk.lwm2m.client.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.pk.lwm2m.client.model.ProductStatus;

public class ProductStatusDAO {
	
private static EntityManager em;
	
	public void insertProductStatus(ProductStatus productStatus){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(productStatus);
		em.getTransaction().commit();	        
		em.close();
		
	}
	
	public ProductStatus getProductStatus(long deviceId, long productId){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		String selectQuery = "from ProductStatus p WHERE p.device.id=:arg0 AND p.product.id=:arg1";
		
		Query query = em.createQuery(selectQuery);
		query.setParameter("arg0", deviceId);
		query.setParameter("arg1", productId);
		ProductStatus ps = (ProductStatus) query.getSingleResult();
		
		em.getTransaction().commit();	        
		em.close();
		
		return ps;
		
	}
	
	public void updateProductStatusQuantity(long deviceId, long productId, int quantity ){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		String selectQuery = "from ProductStatus p WHERE p.device.id=:arg0 AND p.product.id=:arg1";
		
		Query query = em.createQuery(selectQuery);
		query.setParameter("arg0", deviceId);
		query.setParameter("arg1", productId);
		ProductStatus ps = (ProductStatus) query.getSingleResult();
		
		if(ps != null){
			ps.setQuantity(quantity);
		}

		em.getTransaction().commit();	        
		em.close();
		
	}
	
	public boolean deleteProductStatus(long deviceId, long productId){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		String selectQuery = "from ProductStatus p WHERE p.device.id=:arg0 AND p.product.id=:arg1";
		
		Query query = em.createQuery(selectQuery);
		query.setParameter("arg0", deviceId);
		query.setParameter("arg1", productId);
		ProductStatus ps = (ProductStatus) query.getSingleResult();
		
		System.out.println(ps.getId());
		
		boolean result = false;
		
		if(ps != null){
			em.remove(ps);
			result = true;
		}		
		
		em.getTransaction().commit();	        
		em.close();
		
		return result;
	}

}
