package com.pk.lwm2m.server.dao;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.pk.lwm2m.client.model.Device;

public class EntityDAO {
	/**
	 * Inserts the device for successful registration to the database
	 * @param device
	 * @return
	 */
	public boolean insertRegisteredDevice(Device device){
		
		MongoClient mongoClient = null;
		boolean isInsertSuccessful = false;
		try{
			mongoClient = new MongoClient( "localhost" , 27017 );
			MongoDatabase mongoDB = mongoClient.getDatabase("lwm2m_server");
			
			Document document = new Document();
			document.put("uuid", device.getUuid());
			document.put("model_number", device.getModelNumber());
			document.put("serial_numner", device.getSerialNumber());
			document.put("isRegistered", true);
			
			MongoCollection<Document> registrationColl = mongoDB.getCollection("registration");
			
			registrationColl.insertOne(document);
			isInsertSuccessful = true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			mongoClient.close();
		}
		
		return isInsertSuccessful;
	}
	
	public void removeRegistrationDocument(String uuid){
			MongoClient mongoClient = null;
				
			try{
				mongoClient = new MongoClient( "localhost" , 27017 );
				MongoDatabase mongoDB = mongoClient.getDatabase("lwm2m_server");
				MongoCollection<Document> registrationColl = mongoDB.getCollection("registration");
				
				registrationColl.deleteOne(eq("uuid",uuid));
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				mongoClient.close();
			}
	}
	
	public void deregisterDevice(String uuid){
		
		MongoClient mongoClient = null;
		
		try{
			mongoClient = new MongoClient( "localhost" , 27017 );
			MongoDatabase mongoDB = mongoClient.getDatabase("lwm2m_server");
			MongoCollection<Document> registrationColl = mongoDB.getCollection("registration");
			
			registrationColl.updateOne(eq("uuid", uuid),
				combine(set("isRegistered", false)));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			mongoClient.close();
		}
		
	}
	 
}
