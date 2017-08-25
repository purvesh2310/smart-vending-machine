package com.pk.lwm2m.client;

import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.pk.lwm2m.client.dao.EntityDAO;
import com.pk.lwm2m.client.model.Device;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class PaymentModule {
	
	public static void startPaymentModule(Scanner scanner){
		
		System.out.println("Choose one of the following options for Payment Module:");
		System.out.println("1. Get Payment Module Information");
		System.out.println("2. Upgrade/Downgrade Payment Plan");
		
		String choice = scanner.nextLine();
		
		if(choice.equals("1")){
			getPaymentModuleInformation(scanner);
		}else if(choice.equals("2")){
			changePaymentPlan(scanner);
		}else{
			System.out.println("ERROR: Invalid option selection.");
			return;
		}
	}
	
	public static void getPaymentModuleInformation(Scanner scanner){
		
		System.out.println("Enter Vending Machine UUID:");
		String uuid = scanner.nextLine();
		
		Client client = Client.create();
    	
    	String URL = "http://localhost:8080/lwm2m/api/server/getPaymentInformation" + "?uuid=" + uuid;
        
        WebResource webResource = client.resource(URL);
       
        ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
        	        
        if(response.getStatus() == 200){
        	String result = response.getEntity(String.class);
        	if(!result.equals("FAILURE")){
        		
        		JSONParser parser = new JSONParser();
        		try {
					JSONObject paymentObj = (JSONObject)parser.parse(result);
					
					System.out.println("========== Payment Information ==========");
					System.out.println("Client ID: " + paymentObj.get("uuid"));
					System.out.println("Request Count: " + paymentObj.get("request_count"));
					System.out.println("Payment Plan Type: " + paymentObj.get("payment_plan"));
					System.out.println("APIs Accessed: " + paymentObj.get("api_type"));
					System.out.println("Payment Amount: $" + paymentObj.get("payment"));
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
        		
        	} else {
        		System.out.println("RESULT==>" + result);
        	}	
        }
	}
	
	public static void changePaymentPlan(Scanner scanner){
		
		System.out.println("Choose Operation:");
		System.out.println(" 1. Upgrade \n 2. Downgrade");
		
		String choice = scanner.nextLine();
		String paymentPlan;
		
		if(choice.equals("1")){
			paymentPlan = "P";
		}else if(choice.equals("2")){
			paymentPlan = "F";
		}else{
			System.out.println("ERROR: Invalid option selection.");
			return;
		}
		
		System.out.println("Enter Vending Machine UUID:");
		String uuid = scanner.nextLine();
		
		Device device = new Device();
		device.setUuid(uuid);
		device.setPaymentPlan(paymentPlan);
		
		EntityDAO entityDAO = new EntityDAO();
		entityDAO.updateDevice(device);
		
		JSONObject obj = new JSONObject();
    	obj.put("uuid", uuid);
    	obj.put("paymentPlan", paymentPlan);
    	
    	Client client = Client.create();
        
        WebResource webResource = client.resource("http://localhost:8080/lwm2m/api/server/updatePaymentPlan");
       
        ClientResponse response = webResource.type("application/json").
				post(ClientResponse.class,obj.toJSONString());
        
        String result = response.getEntity(String.class);
        if(result.equals("SUCCESS")){
        	System.out.println("SUCCESS==> Payment Plan successfully changed..");
        }
		
	}
}
