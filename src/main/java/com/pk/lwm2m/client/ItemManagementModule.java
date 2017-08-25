package com.pk.lwm2m.client;

import java.util.Scanner;

import com.pk.lwm2m.client.dao.EntityDAO;
import com.pk.lwm2m.client.dao.NotificationStatusDAO;
import com.pk.lwm2m.client.dao.ProductDAO;
import com.pk.lwm2m.client.dao.ProductStatusDAO;
import com.pk.lwm2m.client.model.Device;
import com.pk.lwm2m.client.model.Product;
import com.pk.lwm2m.server.dao.PaymentDAO;

public class ItemManagementModule {
	
	public static void startItemManagementModule(Scanner scanner){
		
		System.out.println("Choose one of the following options for Item Management Module:");
		System.out.println("1. Start LWM2M Server Operation");
		System.out.println("2. Start Vending Machine Operation");
		
		String choice = scanner.nextLine();
		
		if(choice.equals("1")){
			performServerOperationForItemManagement(scanner);
		}else if(choice.equals("2")){
			performVendingMachineOperation(scanner);
		}else{
			System.out.println("ERROR: Invalid option selection.");
			return;
		}
	}
	
	public static void performServerOperationForItemManagement(Scanner scanner){
		
		System.out.println("Select an operation:");
		System.out.println("1. Low Balance Alert Mechanism");
		System.out.println("2. Refill Item in Vending Machine");
		System.out.println("3. Remove Item from Vending Machine");
		
		String choice = scanner.nextLine();
		
		if(choice.equals("1")){
			
			// CONFIG: Minimum product balance for alert to server 
			String objectId = "ProductStatus";
			String resourceId = "quantity";
			String param = "lt";
			String value = "5";
			
			System.out.println("Processing....");
			
			// OPERATION: Write Attribute used for notifying the server
			DeviceManagement.performWriteAttributeOperation(objectId, resourceId, param, value);
			
			// OPERATION: Observing changes to the objectId
			InformationReporting.performObserveOperation(objectId);
			
		}else if(choice.equals("2")){
			
			System.out.println("Enter Vending Machine UUID:");
			String uuid = scanner.nextLine();
			
			System.out.println("Enter Product Name:");
			String productName = scanner.nextLine();
			
			System.out.println("Enter Product Quantity:");
			String quantity = scanner.nextLine();
			
			System.out.println("Processing....");
			
			PaymentDAO paymentDAO = new PaymentDAO();
			
			boolean isRequestCallValid = paymentDAO.checkRequestCapForClient(uuid);
			if(!isRequestCallValid){
				System.out.println("FAILURE: Request Cap Reached. Please UPGRADE your plan");
				return;
			}
			
			paymentDAO.updateRequestCount(uuid);
			paymentDAO.checkAndAddFeatureAPIType(uuid);
			
			EntityDAO entityDao = new EntityDAO();
	    	Device device = entityDao.getDeviceFromUUID(uuid);
			
			ProductDAO productDAO = new ProductDAO();
			Product product = productDAO.getProductByName(productName);
			
			ProductStatusDAO psDAO = new ProductStatusDAO();
			// OPERATION: Updating the product quantity as a part of refilling items
			psDAO.updateProductStatusQuantity(device.getId(),product.getId(), Integer.parseInt(quantity));
			
			System.out.println("SUCCESS ==> Product Refilled Successfully");
			
			NotificationStatusDAO nsDAO = new NotificationStatusDAO();
			// OPERATION: Canceling observation after a refill
			boolean result = nsDAO.updateNotificationStatus("ProductStatus", false);
			
			if(result){
				System.out.println("SUCCESS ==> Observation Cancelled");
			} else {
				System.out.println("FAILURE ==> Could Not Cancel Observation");
			}
			
		}else if(choice.equals("3")){
			
			System.out.println("Enter Vending Machine UUID:");
			String uuid = scanner.nextLine();
			
			System.out.println("Enter Product Name:");
			String productName = scanner.nextLine();
			
			System.out.println("Processing....");
			
			PaymentDAO paymentDAO = new PaymentDAO();
			
			boolean isRequestCallValid = paymentDAO.checkRequestCapForClient(uuid);
			if(!isRequestCallValid){
				System.out.println("FAILURE: Request Cap Reached. Please UPGRADE your plan");
				return;
			}
			
			paymentDAO.updateRequestCount(uuid);
			paymentDAO.checkAndAddFeatureAPIType(uuid);
			
			EntityDAO entityDao = new EntityDAO();
	    	Device device = entityDao.getDeviceFromUUID(uuid);

			ProductDAO productDAO = new ProductDAO();
			Product product = productDAO.getProductByName(productName);
			
			ProductStatusDAO psDAO = new ProductStatusDAO();
			boolean result = psDAO.deleteProductStatus(device.getId(), product.getId());
			
			if(result){
				System.out.println("SUCESS==>" + productName + " removed from Vending Machine " + device.getUuid());
			}else{
				System.out.println("FAILURE==> Item could not be removed.");
			}
		}else{
			System.out.println("ERROR: Invalid option selection.");
			return;
		}
	}
	
	public static void performVendingMachineOperation(Scanner scanner){
		
		System.out.println("Enter Vending Machine UUID:");
		String uuid = scanner.nextLine();
		
		System.out.println("Enter Product Name:");
		String productName = scanner.nextLine();
		
		System.out.println("Enter Product Quantity:");
		String quantity = scanner.nextLine();
		
		System.out.println("Processing....");
		
		EntityDAO entityDao = new EntityDAO();
    	Device device = entityDao.getDeviceFromUUID(uuid);
		
		ProductDAO productDAO = new ProductDAO();
		Product product = productDAO.getProductByName(productName);
		
		ProductStatusDAO psDAO = new ProductStatusDAO();
		
		// OPERATION: Updating the product quantity as a part of sending the notification to server
		psDAO.updateProductStatusQuantity(device.getId(),product.getId(), Integer.parseInt(quantity));
		
	}

}
