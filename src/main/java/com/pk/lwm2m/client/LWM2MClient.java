package com.pk.lwm2m.client;

import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pk.lwm2m.client.dao.EntityDAO;
import com.pk.lwm2m.client.dao.LWM2MSecurityDAO;
import com.pk.lwm2m.client.dao.LWM2MServerDAO;
import com.pk.lwm2m.client.model.Device;
import com.pk.lwm2m.client.model.LWM2MSecurity;
import com.pk.lwm2m.client.model.LWM2MServer;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class LWM2MClient {
	
	public static String BOOTSTRAP_SERVER_URL = "http://localhost:8080/lwm2m/api/bs/perform";
		  
	    public static void main(String[] args) {
	    		    	
	    	System.setProperty("org.jboss.logging.provider", "log4j");
	    	
			Scanner scanner = new Scanner(System.in);
			
			while(true){

				System.out.println("=================================");
				System.out.println("      SMART VENDING MACHINE      ");
				System.out.println("=================================");
			
				System.out.println("Select an Option: \n 1. Bootstrap and Registration \n 2. De-registration");
				System.out.println(" 3. Item Management Module \n 4. Currency Managment Module");
				System.out.println(" 5. Payment Module \n 6. Utilities");
				System.out.println(" 0. EXIT");
				
				String choice = scanner.nextLine();
				
				if(choice.equals("1")){
					doBootstrap(scanner);
				}else if(choice.equals("2")){
					doDeRegistration(scanner);
				}else if(choice.equals("3")){
					ItemManagementModule.startItemManagementModule(scanner);
				} else if(choice.equals("4")){
					CurrencyManagementModule.startCurrencyManagementModule(scanner);
				}else if(choice.equals("5")){
					PaymentModule.startPaymentModule(scanner);
				}else if (choice.equals("6")){
					
					System.out.println(" 1. Device Management \n 2. Information Reporting");
					String utilityChoice = scanner.nextLine();
					
					if(utilityChoice.equals("1")){
						DeviceManagement.startDeviceManagement(scanner);
					} else if(utilityChoice.equals("2")){
						InformationReporting.startInformationReporting(scanner);
					} else{
						System.out.println("ERROR: Select Valid Option.");
					}
					
				}else if(choice.equals("0")){
					break;
				}
				else{
					System.out.println("ERROR: Select Valid Option.");
				}
			}
			
			scanner.close();
	    }
	    
	    /**
	     * Initiates the Bootstrap procedure for vending machine
	     */
	    public static void doBootstrap(Scanner scanner){
	    	Client client = Client.create();
	        
	        WebResource webResource = client.resource(BOOTSTRAP_SERVER_URL);
			
			JSONObject obj = new JSONObject();
	    	System.out.println("Enter Device UUID: ");
			String uuid = scanner.nextLine();
			
			obj.put("uuid", uuid);

		    ClientResponse response = webResource.type("application/json")
		    		.post(ClientResponse.class, obj.toJSONString());

			String output = response.getEntity(String.class); 
			
			JSONParser parser = new JSONParser();
			JSONObject json = null;
			try {
				json = (JSONObject) parser.parse(output);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			String serverUrl = (String)json.get("serverUrl");
			String serverUuid = (String)json.get("uuid");
			String pin = (String)json.get("password");
			long lifetime = Long.parseLong((String)json.get("lifetime"));
			
			
			LWM2MServerDAO d = new LWM2MServerDAO();
			LWM2MServer s = new LWM2MServer();
			s.setLifetime(lifetime);
		
			int serverid = d.saveServerInformation(s);
			
			LWM2MSecurity sec = new LWM2MSecurity();
			sec.setBootstrapServer(false);
			sec.setClientIdentity(pin);
			sec.setServerIdentity(serverUuid);
			sec.setServerURI(serverUrl);
			sec.setShortServerId(serverid);
			
			LWM2MSecurityDAO d1 = new LWM2MSecurityDAO();
			LWM2MSecurity savedSec = d1.saveBootstrapInformation(sec);
			
			System.out.println("Bootstrap Finished... ");
			
			System.out.println("Press a key to start registration...");
			scanner.nextLine();
	
			doRegistration(uuid, pin, savedSec, serverUrl, scanner);
		}
	    
	    /**
	     * Performs the registration of the device to LWM2M server
	     * @param uuid
	     * @param serverURL
	     */
	    public static void doRegistration(String uuid, String pin, LWM2MSecurity sec, String serverURL, Scanner scanner){
	    	
	    	Client client = Client.create();
		        
	    	serverURL = "http://localhost:8080" + serverURL;
	    	
	        WebResource webResource = client.resource(serverURL);
			
			EntityDAO clientDAO = new EntityDAO();
			Device device = clientDAO.getDeviceFromUUID(uuid);
		
			if(device == null){
				device = new Device();
				
				device.setUuid(uuid);
				device.setSecurity(sec);
				device.setIsDeviceOnline(1);
				
				System.out.println("Enter Device Manufacturer: ");
				String manufacturer = scanner.nextLine();
				device.setManufacturer(manufacturer);
				
				System.out.println("Enter Device Type: ");
				String deviceType = scanner.nextLine();
				device.setDeviceType(deviceType);
				
				System.out.println("Enter Device Model Number: ");
				String modelNumber = scanner.nextLine();
				device.setModelNumber(modelNumber);
				
				System.out.println("Enter Device Serial Number: ");
				String serialNumber = scanner.nextLine();
				device.setSerialNumber(serialNumber);
				
				System.out.println("Enter Payment Plan Type: P for Premium/ F for free:");
				String paymentPlan = scanner.nextLine();
				device.setPaymentPlan(paymentPlan);
				
			}
			
			Gson gson = new GsonBuilder().create();
			String json = gson.toJson(device);

			ClientResponse response = webResource.type("application/json").
					post(ClientResponse.class,json);
			
			if(response.getStatus() == 200){
				String result = response.getEntity(String.class);
				System.out.println("Registration Finished... ");
				System.out.println("Result ===>" + result);
				if(result.equals("SUCCESS")){
					device.setRegistered(true);
				}else{
					device.setRegistered(false);
				}
				clientDAO.insertRegisterdDevice(device);
			}
	    }
	    
	    /**
	     * Dr-Registers the client from the server
	     * @param scanner
	     */
	    public static void doDeRegistration(Scanner scanner){
	    	
	    	System.out.println("Enter Device UUID: ");
			String uuid = scanner.nextLine();
			
			System.out.println("Enter Device PIN: ");
			String pin = scanner.nextLine();
			
			JSONObject obj = new JSONObject();
			obj.put("uuid", uuid);
			obj.put("pin", pin);

			Client client = Client.create();
	        
	        WebResource webResource = client.resource("http://localhost:8080/lwm2m/api/server/deregister");
	       
	        ClientResponse response = webResource.type("application/json").
					post(ClientResponse.class,obj.toJSONString());
	        
	        if(response.getStatus() == 200){
				String result = response.getEntity(String.class);
	
				if(result.equals("SUCCESS")){
					
					EntityDAO entityDAO = new EntityDAO();
					entityDAO.deregisterDevice(uuid);
					
					System.out.println("RESULT ==> SUCESS");
					
				}else{
					System.out.println("ERROR ==> Deregestration could not be comnpleted");
				}	
	        }	
	    }
}
