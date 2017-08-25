package com.pk.lwm2m.client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.pk.lwm2m.client.dao.DeviceManagementDAO;
import com.pk.lwm2m.client.dao.EntityDAO;
import com.pk.lwm2m.client.dao.ProductDAO;
import com.pk.lwm2m.client.dao.ProductStatusDAO;
import com.pk.lwm2m.client.model.Device;
import com.pk.lwm2m.client.model.Product;
import com.pk.lwm2m.client.model.ProductStatus;
import com.pk.lwm2m.client.model.WriteAttribute;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class DeviceManagement {
	
	public static void startDeviceManagement(Scanner scanner){
		
		System.out.println(" 1. Read \n 2. Discover \n 3. Write \n 4. Write Attribute");
		System.out.println(" 5. Execute \n 6. Create \n 7. Delete");
		String choice = scanner.nextLine();
		
		if(choice.equals("1")){
			System.out.println("Enter Object Id:");
			String objectId = scanner.nextLine();
			
			System.out.println("Enter Object Instance Id:");
			String objectInstanceId = scanner.nextLine();
			
			System.out.println("Enter Resource Id:");
			String resourceId = scanner.nextLine();
			
			DeviceManagementDAO dmDAO = new DeviceManagementDAO();
			Object readObj = dmDAO.performReadOperation(objectId, objectInstanceId, resourceId);
			
			if(readObj != null){			
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("value", readObj);
				
				Client client = Client.create();
		        
		        WebResource webResource = 
		        		client.resource("http://localhost:8080/lwm2m/api/server/sendDataToServer");
		       
		        ClientResponse response = webResource.type("application/json").
						post(ClientResponse.class,jsonObj.toJSONString());
		        
		        if(response.getStatus() == 200){
		        	System.out.println("SUCCESS ==> Read Data Sent Successfully");
		        } else {
		        	System.out.println("FAILURE ==> Read Data Could Not Be Sent");
		        }
			} else {
				System.out.println("FAILURE ==> Requested Data/Resource Could Not Be Found");
			}
		}
		else if(choice.equals("2")){
			
			System.out.println("Enter Object Id:");
			String objectId = scanner.nextLine();
			
			DeviceManagementDAO dmDAO = new DeviceManagementDAO();
			List<String> discoverList = dmDAO.performDiscoverOperation(objectId);
			
			Gson gson = new Gson();
			String discoverListString = gson.toJson(discoverList);;
			
			Client client = Client.create();
	        
	        WebResource webResource = 
	        		client.resource("http://localhost:8080/lwm2m/api/server/sendDataToServer");
	       
	        ClientResponse response = webResource.type("application/json").
					post(ClientResponse.class,discoverListString);
	        
	        if(response.getStatus() == 200){
	        	System.out.println("SUCCESS ==> Discover Data Sent Successfully");
	        } else {
	        	System.out.println("FAILURE ==> Discover Data Could Not Be Sent");
	        }			
		}
		else if(choice.equals("3")){
			
			System.out.println("Enter Object Id:");
			String objectId = scanner.nextLine();
			
			System.out.println("Enter Object Instance Id:");
			String objectInstanceId = scanner.nextLine();
			
			System.out.println("Enter Resource Id:");
			String resourceId = scanner.nextLine();
			
			System.out.println("Enter new value:");
			String newVal = scanner.nextLine();
			
			DeviceManagementDAO dmDAO = new DeviceManagementDAO();
			int success = dmDAO.performWriteOperation(objectId, objectInstanceId, resourceId, newVal);
			
			if(success > 0){
				System.out.println("SUCESS ==> WRITE operation performed successfully");
			} else {
				System.out.println("FAILURE ==> WRITE operation cannot be performed");
			}
		} else if(choice.equals("4")){
			
			System.out.println("Enter Object Id:");
			String objectId = scanner.nextLine();
			
			System.out.println("Enter Resource Id:");
			String resourceId = scanner.nextLine();
			
			System.out.println("Enter Parameter:");
			String param = scanner.nextLine();
			
			System.out.println("Enter Parameter value:");
			String value = scanner.nextLine();
			
			performWriteAttributeOperation(objectId, resourceId, param, value);
			
			
		} else if(choice.equals("5")){
			
			System.out.println("Enter Object Id:");
			String objectId = scanner.nextLine();
			
			System.out.println("Enter Object Instance Id:");
			String objectInstanceId = scanner.nextLine();
			
			System.out.println("Enter Resource Id:");
			String resourceId = scanner.nextLine();
			
			DeviceManagementDAO dmDAO = new DeviceManagementDAO();
			
			int result = dmDAO.performShutDownExecuteOperation(objectId, objectInstanceId, resourceId);
			
			if(result>0){
				System.out.println("SUCCESS ==> Device Is Turned Off Successfully");
			} else{
				System.out.println("FAILURE ==> Request Device Could Not Be Turned Off");
			}
			
		} else if(choice.equals("6")){
			
			System.out.println("Add Device UUID:");
	    	String uuid = scanner.nextLine();
	    	
	    	System.out.println("Add product Id");
	    	String productId = scanner.nextLine();
	    	
	    	System.out.println("Add product quantity");
	    	int quantity = Integer.parseInt(scanner.nextLine());
	    	
	    	System.out.println("Add expiry date in MM-dd-yyyy format");
	    	String inputString = scanner.nextLine();
	    	
	    	DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	    	Date inputDate = null;
	    	try {
	    		inputDate = dateFormat.parse(inputString);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
	    	
	    	EntityDAO entityDao = new EntityDAO();
	    	Device device = entityDao.getDeviceFromUUID(uuid);
	    	
	    	ProductDAO productDAO = new ProductDAO();
	    	Product product = productDAO.findProductById(Long.parseLong(productId));

	    	
	    	ProductStatus status = new ProductStatus();
	    	status.setQuantity(quantity);
	    	status.setExpiryDate(inputDate);
	    	status.setDevice(device);
	    	status.setProduct(product);
	    	
	    	ProductStatusDAO psDAO = new ProductStatusDAO();
	    	psDAO.insertProductStatus(status);
			
		}else if(choice.equals("7")){
			
			System.out.println("Enter Object Id:");
			String objectId = scanner.nextLine();
			
			System.out.println("Enter Object Instance Id:");
			String objectInstanceId = scanner.nextLine();
			
			DeviceManagementDAO dmDAO = new DeviceManagementDAO();
			int result = dmDAO.performDeleteOperation(objectId, objectInstanceId);
			
			if(result>0){
				System.out.println("SUCCESS==> Object Instance Deleted Successfully..");
			} else {
				System.out.println("FAILURE==> Object Instance Cannot Be Deleted..");
			}
		} else {
			System.out.println("Please enter valid choice. Try Again..!!");
		}
	}
	
	public static void performWriteAttributeOperation(String objectId, String resourceId, String param, String value){

		WriteAttribute writeAtt = new WriteAttribute();
		
		writeAtt.setObjectId(objectId);
		writeAtt.setResourceId(resourceId);
		writeAtt.setParam(param);
		writeAtt.setValue(Long.parseLong(value));
		
		DeviceManagementDAO dmDAO = new DeviceManagementDAO();
		WriteAttribute savedWriteAtt = dmDAO.performWriteAttributeOperation(writeAtt);
		
		if(savedWriteAtt != null){
			System.out.println("SUCCESS ==> WRITE ATTRIBUTE performed successfully");
		}else {
			System.out.println("FAILURE ==> WRITE ATTRIBUTE Could Not Be Performed");
		}
	}

}
