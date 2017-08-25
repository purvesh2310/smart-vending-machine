package com.pk.lwm2m.client;

import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONObject;

import com.pk.lwm2m.client.dao.DepositDAO;
import com.pk.lwm2m.client.dao.EntityDAO;
import com.pk.lwm2m.client.dao.ProductDAO;
import com.pk.lwm2m.client.dao.ProductStatusDAO;
import com.pk.lwm2m.client.dao.TransactionDAO;
import com.pk.lwm2m.client.model.Deposit;
import com.pk.lwm2m.client.model.Device;
import com.pk.lwm2m.client.model.Product;
import com.pk.lwm2m.client.model.ProductStatus;
import com.pk.lwm2m.client.model.Transaction;
import com.pk.lwm2m.server.dao.PaymentDAO;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class CurrencyManagementModule {
	
	public static void startCurrencyManagementModule(Scanner scanner){
		
		System.out.println("Choose one of the following options for Currency Management Module:");
		System.out.println("1. Buy Items from Vending Machine");
		System.out.println("2. Get Deposit Currency Information");
		
		String choice = scanner.nextLine();
		
		if(choice.equals("1")){
			buyItems(scanner);
		}else if(choice.equals("2")){
			getDepositInformation(scanner);
		}else{
			System.out.println("ERROR: Invalid option selection.");
			return;
		}
		
	}
	
	public static void buyItems(Scanner scanner){
		
		System.out.println("Enter Vending Machine UUID:");
		String uuid = scanner.nextLine();
		
		System.out.println("Enter Product Name:");
		String productName = scanner.nextLine();
		
		System.out.println("Enter Product Quantity:");
		String quantity = scanner.nextLine();

		ProductDAO productDAO = new ProductDAO();
		Product product = productDAO.getProductByName(productName);
		
		float totalPrice = Integer.parseInt(quantity)*product.getPrice();
		
		System.out.println("Total Amount To Pay: $" + totalPrice);
		
		System.out.println("$1: ");
		int oneUnit = Integer.parseInt(scanner.nextLine());
		
		System.out.println("$5: ");
		int fiveUnit = Integer.parseInt(scanner.nextLine());
		
		System.out.println("$10: ");
		int tenUnit = Integer.parseInt(scanner.nextLine());
		
		System.out.println("$20: ");
		int twentyUnit = Integer.parseInt(scanner.nextLine());
		
		System.out.println("$100: ");
		int hundredUnit = Integer.parseInt(scanner.nextLine());
		
		System.out.println("Processing....");
		
		EntityDAO entityDao = new EntityDAO();
    	Device device = entityDao.getDeviceFromUUID(uuid);
    	
    	Transaction transaction = new Transaction();
		transaction.setAmount(totalPrice);
		transaction.setDevice(device);
		
		System.out.println("Saving Transaction...");
		TransactionDAO transactionDAO = new TransactionDAO();
		Transaction savedTransaction = transactionDAO.saveTransaction(transaction);
		
		System.out.println("Saving Currency Deposit Information...");
		Deposit deposit = new Deposit();
		deposit.setOneUnit(oneUnit);
		deposit.setFiveUnit(fiveUnit);
		deposit.setTenUnit(tenUnit);
		deposit.setTwentyUnit(twentyUnit);
		deposit.setHundredUnit(hundredUnit);
		deposit.setTransaction(savedTransaction);
		
		DepositDAO depositDAO = new DepositDAO();
		depositDAO.saveDeposit(deposit);
		
		ProductStatusDAO psDAO = new ProductStatusDAO();
		
		ProductStatus ps = psDAO.getProductStatus(device.getId(), product.getId());
		int updatedQuantity = ps.getQuantity() - Integer.parseInt(quantity);
		
		psDAO.updateProductStatusQuantity(device.getId(), product.getId(),updatedQuantity);
		
		System.out.println("SUCCESS ==> Transaction completed.");
	}
	
	public static void getDepositInformation(Scanner scanner){
		
		System.out.println("Enter Vending Machine UUID:");
		String uuid = scanner.nextLine();
		
		System.out.println("Processing...");
		
		PaymentDAO paymentDAO = new PaymentDAO();
		
		boolean isRequestCallValid = paymentDAO.checkRequestCapForClient(uuid);
		if(!isRequestCallValid){
			System.out.println("FAILURE: Request Cap Reached. Please UPGRADE your plan");
			return;
		}
		
		EntityDAO entityDao = new EntityDAO();
    	Device device = entityDao.getDeviceFromUUID(uuid);
    	
    	TransactionDAO transactionDAO = new TransactionDAO();
    	List<Transaction> transactionList = transactionDAO.getAllTransactionForVendingMachine(device.getId());
    	
    	DepositDAO depositDAO = new DepositDAO();
    	
    	int oneUnit=0,fiveUnit=0,tenUnit=0,twentyUnit=0,hundredUnit=0;
    	
    	for(Transaction aTransction: transactionList){
    		Deposit deposit = depositDAO.getDepositInformation(aTransction.getId());
    		oneUnit = oneUnit + deposit.getOneUnit();
    		fiveUnit = fiveUnit + deposit.getFiveUnit();
    		tenUnit = tenUnit + deposit.getTenUnit();
    		twentyUnit = twentyUnit + deposit.getTwentyUnit();
    		hundredUnit = hundredUnit + deposit.getHundredUnit();
    	}
    	
    	System.out.println("Currency Deposit Summary:");
    	System.out.println("$1:   " + oneUnit +" = $" + oneUnit*1);
    	System.out.println("$5:   " + fiveUnit +" = $" + fiveUnit*5);
    	System.out.println("$10:  " + tenUnit +" = $" + tenUnit*10);
    	System.out.println("$20:  " + twentyUnit +" = $" + twentyUnit*20);
    	System.out.println("$100: " + hundredUnit +" = $" + hundredUnit*100);
    	
    	int total = oneUnit*1 + fiveUnit*5 + tenUnit*10 + twentyUnit*20 + hundredUnit*100;
    	
    	System.out.println("=================================");
    	System.out.println("Total = $" + total);
    	
    	JSONObject jsonObj = new JSONObject();
    	jsonObj.put("totalCurrencyValue", total);
    	jsonObj.put("uuid", device.getUuid());
    	
    	Client client = Client.create();
        
        WebResource webResource = 
        		client.resource("http://localhost:8080/lwm2m/api/server/sendDataToServer");
       
        ClientResponse response = webResource.type("application/json").
				post(ClientResponse.class,jsonObj.toJSONString());
        
        String result = response.getEntity(String.class);
    	
		if(result.equals("SUCCESS")){
			System.out.println("SUCCESS ==> Read Data Sent Successfully"); 
		}else{
			System.out.println(result);
		}
	}
}
