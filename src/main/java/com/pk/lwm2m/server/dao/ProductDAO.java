package com.pk.lwm2m.server.dao;

import java.time.LocalDateTime;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import static com.mongodb.client.model.Updates.*;


public class ProductDAO {
	
public void saveProduct(String uuid, String name, String manufacturer, int quantity){
		
		MongoClient mongoClient = null;
		try{
			mongoClient = new MongoClient( "localhost" , 27017 );
			MongoDatabase mongoDB = mongoClient.getDatabase("lwm2m_server");
			
			Document document = new Document();
			document.put("client_uuid", uuid);
			document.put("name",name);
			document.put("manufacturer", manufacturer);
			document.put("quantity", quantity);
			
			LocalDateTime currentTime = LocalDateTime.now();
			 
			document.put("timestamp", currentTime.toString());
			
			MongoCollection<Document> productColl = mongoDB.getCollection("product");
			
			productColl.insertOne(document);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			mongoClient.close();
		}
		
	}

public void updateProductQuanity(String uuid, String name, int quantity){
	
	MongoClient mongoClient = null;
	try{
		mongoClient = new MongoClient( "localhost" , 27017 );
		MongoDatabase mongoDB = mongoClient.getDatabase("lwm2m_server");
		MongoCollection<Document> productColl = mongoDB.getCollection("product");
		
		LocalDateTime currentTime = LocalDateTime.now();

		productColl.updateOne(
                Filters.and(eq("client_uuid", uuid), eq("name",name)),
                combine(Updates.set("quantity", quantity), Updates.set("timestamp", currentTime.toString() )));		
		
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		mongoClient.close();
	}
	
}

}
