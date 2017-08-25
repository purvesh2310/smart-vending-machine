package com.pk.lwm2m.server.dao;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;

public class ServerDAO {
 
	public Document getServerInformation(String serverUuid){
		
		MongoClient mongoClient = null;
		Document serverDocument = null;
		
		try{
			mongoClient = new MongoClient( "localhost" , 27017 );
			MongoDatabase mongoDB = mongoClient.getDatabase("lwm2m_server");
			MongoCollection<Document> serverCollection = mongoDB.getCollection("server");
			
			serverDocument = serverCollection.find(eq("uuid",serverUuid)).first();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			mongoClient.close();
		}
		
		return serverDocument;
	}
}
