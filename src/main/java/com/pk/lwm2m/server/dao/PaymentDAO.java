package com.pk.lwm2m.server.dao;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import static com.mongodb.client.model.Updates.*;
import com.pk.lwm2m.client.model.Device;

public class PaymentDAO {
	
	public void createPaymentDocument(Device device){
		
		MongoClient mongoClient = null;
		
		try{
			mongoClient = new MongoClient( "localhost" , 27017 );
			MongoDatabase mongoDB = mongoClient.getDatabase("lwm2m_server");
			
			List<String> apiTypeAccessed = new ArrayList<String>();
			apiTypeAccessed.add("CORE");
			
			Document document = new Document();
			document.put("uuid", device.getUuid());
			document.put("payment_plan", device.getPaymentPlan());
			document.put("request_count", 0);
			document.put("payment",0);
			document.put("api_type",apiTypeAccessed);
			
			MongoCollection<Document> paymentColl = mongoDB.getCollection("payment");
			
			paymentColl.insertOne(document);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			mongoClient.close();
		}
		
	}
	
	public boolean checkRequestCapForClient(String uuid){
		
		MongoClient mongoClient = null;
		Document paymentDoc = null;
		long requestCount;
		String paymentPlan = null;
		
		try{
			mongoClient = new MongoClient( "localhost" , 27017 );
			MongoDatabase mongoDB = mongoClient.getDatabase("lwm2m_server");
			MongoCollection<Document> paymentColl = mongoDB.getCollection("payment");
			
			paymentDoc = paymentColl.find(eq("uuid",uuid)).first();
			requestCount = paymentDoc.getInteger("request_count");
			paymentPlan = paymentDoc.getString("payment_plan");
			if(paymentPlan.equals("F") && requestCount < 100){
				return true;
			}else if(paymentPlan.equals("P")){
				return true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			mongoClient.close();
		}
		
		return false;
	}
	
	public void updateRequestCount(String uuid){
		
		MongoClient mongoClient = null;
		try{
			mongoClient = new MongoClient( "localhost" , 27017 );
			MongoDatabase mongoDB = mongoClient.getDatabase("lwm2m_server");
			MongoCollection<Document> paymentColl = mongoDB.getCollection("payment");

			paymentColl.updateOne(
	               eq("uuid", uuid),
	                combine(Updates.inc("request_count", 1)));	
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			mongoClient.close();
		}
	}
	
	public Document getPaymentInformation(String uuid){
		
		MongoClient mongoClient = null;
		Document paymentDoc = null;
		
		try{
			mongoClient = new MongoClient( "localhost" , 27017 );
			MongoDatabase mongoDB = mongoClient.getDatabase("lwm2m_server");
			MongoCollection<Document> paymentColl = mongoDB.getCollection("payment");
			
			paymentDoc = paymentColl.find(eq("uuid",uuid)).first();
			
			String paymentPlan = paymentDoc.getString("payment_plan");
			
			if(paymentPlan.equals("P")){
				int requestCount = paymentDoc.getInteger("request_count", 0);
				double charges = requestCount * 0.5;
				
				paymentColl.updateOne(eq("uuid", uuid),
						combine(set("payment", charges)));
				
				paymentDoc = paymentColl.find(eq("uuid",uuid)).first();
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			mongoClient.close();
		}
		
		return paymentDoc;
		
	}
	
	public void updatePaymentPlan(String uuid, String paymentPlan){
		
		MongoClient mongoClient = null;
		try{
			mongoClient = new MongoClient( "localhost" , 27017 );
			MongoDatabase mongoDB = mongoClient.getDatabase("lwm2m_server");
			MongoCollection<Document> paymentColl = mongoDB.getCollection("payment");

			paymentColl.updateOne(eq("uuid", uuid),
					combine(set("payment_plan", paymentPlan),set("request_count",0)));		
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			mongoClient.close();
		}
	}
	
	public void checkAndAddFeatureAPIType(String uuid){
		
		MongoClient mongoClient = null;
		Document paymentDoc = null;
		
		try{
			mongoClient = new MongoClient( "localhost" , 27017 );
			MongoDatabase mongoDB = mongoClient.getDatabase("lwm2m_server");
			MongoCollection<Document> paymentColl = mongoDB.getCollection("payment");
			
			paymentDoc = paymentColl.find(eq("uuid",uuid)).first();
			List<String> apis = (List<String>) paymentDoc.get("api_type");
			
			boolean isFeatureAPIAccessed = apis.contains("FEATURE");
			
			if(!isFeatureAPIAccessed){
				apis.add("FEATURE");
				paymentColl.updateOne(eq("uuid", uuid),
						combine(set("api_type", apis)));	
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			mongoClient.close();
		}
	}

}
