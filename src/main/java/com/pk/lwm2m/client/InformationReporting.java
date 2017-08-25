package com.pk.lwm2m.client;

import java.util.Scanner;

import com.pk.lwm2m.client.dao.EntityDAO;
import com.pk.lwm2m.client.dao.NotificationStatusDAO;
import com.pk.lwm2m.client.model.Device;
import com.pk.lwm2m.client.model.NotificationStatus;

public class InformationReporting {
	
	public static void startInformationReporting(Scanner scanner){
		
		System.out.println(" 1. Observe \n 2. Notify \n 3. Cancel Observation");
		String choice = scanner.nextLine();
		
		if(choice.equals("1")){
			
			System.out.println("Enter Object Id:");
			String objectId = scanner.nextLine();
			
			performObserveOperation(objectId);
			
		}
		
		else if(choice.equals("2")){
			
			System.out.println("Enter Device UUID:");
			String uuid = scanner.nextLine();
			
			System.out.println("Enter Device Voltage:");
			String voltage = scanner.nextLine();
			
			Device device = new Device();
			
			device.setUuid(uuid);
			device.setPowerSourceVoltage(Integer.parseInt(voltage));
			
			EntityDAO entityDAO = new EntityDAO();
			boolean result = entityDAO.updateDevice(device);
			
			if(result){
				System.out.println("SUCCESS ==> Value Updated Successfully");
			} else {
				System.out.println("FAILURE ==> Could Not Update Value");
			}
			
			
		}else if(choice.equals("3")){
			
			System.out.println("Enter Object Id:");
			String objectId = scanner.nextLine();
			
			NotificationStatusDAO nsDAO = new NotificationStatusDAO();
			boolean result = nsDAO.updateNotificationStatus(objectId, false);
			
			if(result){
				System.out.println("SUCCESS ==> Observation Cancelled");
			} else {
				System.out.println("FAILURE ==> Could Not Cancel Observation");
			}

		}else{
			System.out.println("Please choose valid option..! Try again");
		}
	}
	
	public static void performObserveOperation(String objectId){
		
		NotificationStatusDAO nsDAO = new NotificationStatusDAO();
		boolean isObjectExists = nsDAO.findByObjectId(objectId);
		
		boolean result = false;
		
		if(isObjectExists){
			nsDAO.updateNotificationStatus(objectId, true);
			result = true;
		} else{
			NotificationStatus nsObj = new NotificationStatus();
			nsObj.setObjectId(objectId);
			nsObj.setNotifyStatus(true);
			nsDAO.insertNotificationStatus(nsObj);
			result = true;
		}
		
		if(result){
			System.out.println("SUCCESS ==> Observation Saved Successfully");
		} else {
			System.out.println("FAILURE ==> Observation Could Not be Saved");
		}
		
	}
}
