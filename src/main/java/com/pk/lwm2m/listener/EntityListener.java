package com.pk.lwm2m.listener;

import java.util.List;

import javax.persistence.PostUpdate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pk.lwm2m.client.dao.DeviceManagementDAO;
import com.pk.lwm2m.client.dao.NotificationStatusDAO;
import com.pk.lwm2m.client.model.Device;
import com.pk.lwm2m.client.model.Product;
import com.pk.lwm2m.client.model.ProductStatus;
import com.pk.lwm2m.client.model.WriteAttribute;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class EntityListener {
	
	@PostUpdate
	public void methodInvokedAfterUpdate(Object object) {
		
		Class cls = object.getClass();
		String objectId = cls.getSimpleName();

		NotificationStatusDAO nsDAO = new NotificationStatusDAO();
		boolean isNotificationAllowed = nsDAO.checkNotificationStatus(objectId);
		boolean isNotificationConditionSatisfied = false;
		
		if(isNotificationAllowed){
			
			if(objectId.equals("ProductStatus")){
				
				ProductStatus updatedObject = (ProductStatus) object;
				Device device = new Device();
				device.setId(updatedObject.getDevice().getId());
				device.setUuid(updatedObject.getDevice().getUuid());
				updatedObject.setDevice(device);
				
				Product product = new Product();
				product.setId(updatedObject.getProduct().getId());
				updatedObject.setProduct(product);
				
				DeviceManagementDAO dmDAO = new DeviceManagementDAO();
				List<WriteAttribute> result = dmDAO.findAttribute(objectId);
				
				if(result != null){
					for(WriteAttribute aResult: result){
						if(aResult.getParam().equals("lt")){
							if(updatedObject.getQuantity() < aResult.getValue()){
								isNotificationConditionSatisfied = true;
								break;
							}
						}
					}
				}
				
				if (isNotificationConditionSatisfied) {
					sendNotificationToServer(updatedObject);
				}
				
			}else{
				sendNotificationToServer(object);
			}
		}
    }
	
	public void sendNotificationToServer(Object object){
		
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(object);
		
		Client client = Client.create();
        
        WebResource webResource = 
        		client.resource("http://localhost:8080/lwm2m/api/server/notifyLWM2MServer");
       
        ClientResponse response = webResource.type("application/json").
				post(ClientResponse.class,json);
        
        String result = response.getEntity(String.class);
    	
		if(result.equals("SUCCESS")){
			System.out.println("SUCCESS ==> Server Notified Successfully");
		}else{
			System.out.println(result);
		}
	}
}
