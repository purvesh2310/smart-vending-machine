package com.pk.lwm2m.client.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.pk.lwm2m.client.model.Device;
import com.pk.lwm2m.client.model.Product;

public class ProductDAO {
	
	private static EntityManager em;
	
	public void insertProduct(Product product){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(product);
		em.getTransaction().commit();	        
		em.close();
		
	}
	
	public Product findProductById(long id){
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Product product = em.find(Product.class, id);

		return product;
	}
	
	public Product getProductByName(String name){
	
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LWM2MPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Query query = em.createQuery("from Product p where p.name=:arg1");
		query.setParameter("arg1",name);
		Product result = (Product) query.getSingleResult();
		
		em.getTransaction().commit();	        
		em.close();
		
		return result;
	}

}
