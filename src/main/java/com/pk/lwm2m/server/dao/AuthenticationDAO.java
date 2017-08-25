package com.pk.lwm2m.server.dao;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class AuthenticationDAO {
	
	public void saveClientCredentials(String uuid, String pin){
		
		MongoClient mongoClient = null;
		try{
			mongoClient = new MongoClient( "localhost" , 27017 );
			MongoDatabase mongoDB = mongoClient.getDatabase("lwm2m_server");
			
			Document document = new Document();
			document.put("uuid", uuid);
			document.put("pin", pin);
			
			MongoCollection<Document> registrationColl = mongoDB.getCollection("authentication");
			
			registrationColl.insertOne(document);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			mongoClient.close();
		}
		
	}
	
	public String getClientCredential(String uuid){
		
		MongoClient mongoClient = null;
		Document authenticationDoc = null;
		String pin = null;
		
		try{
			mongoClient = new MongoClient( "localhost" , 27017 );
			MongoDatabase mongoDB = mongoClient.getDatabase("lwm2m_server");
			MongoCollection<Document> authenticationColl = mongoDB.getCollection("authentication");
			
			authenticationDoc = authenticationColl.find(eq("uuid",uuid)).first();
			pin = authenticationDoc.getString("pin");
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			mongoClient.close();
		}
		
		return pin;
	}
	
	public void removeAuthenticationDocument(String uuid){
		
		MongoClient mongoClient = null;
		
		try{
			mongoClient = new MongoClient( "localhost" , 27017 );
			MongoDatabase mongoDB = mongoClient.getDatabase("lwm2m_server");
			MongoCollection<Document> authenticationColl = mongoDB.getCollection("authentication");
			
			authenticationColl.deleteOne(eq("uuid",uuid));
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			mongoClient.close();
		}
	}
}
