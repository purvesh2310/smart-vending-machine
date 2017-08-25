package com.pk.lwm2m.server;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.bson.Document;
import org.glassfish.jersey.server.monitoring.MonitoringStatistics;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.pk.lwm2m.client.model.Device;
import com.pk.lwm2m.server.dao.AuthenticationDAO;
import com.pk.lwm2m.server.dao.EntityDAO;
import com.pk.lwm2m.server.dao.PaymentDAO;
import com.pk.lwm2m.server.dao.ProductDAO;

@Path("/server")
public class LWM2MServer {
	
	@Inject
	Provider<MonitoringStatistics> monitoringStatisticsProvider;

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public String registerClient(String deviceJsonString){
		
		Gson gson = new Gson();
		Device device = gson.fromJson(deviceJsonString,Device.class);
		
		AuthenticationDAO authenticationDAO = new AuthenticationDAO();
		String pin = authenticationDAO.getClientCredential(device.getUuid());
		
		if(!pin.equals(device.getSecurity().getClientIdentity())){
			return "FAILURE";
		}
		else {
		
			EntityDAO serverDAO = new EntityDAO();
			boolean isRegistered = serverDAO.insertRegisteredDevice(device);
			
			PaymentDAO paymentDAO = new PaymentDAO();
			paymentDAO.createPaymentDocument(device);
			
			if(isRegistered){
				return "SUCCESS";
			}
			else{
				return "FAILURE";
			}
		}
	}
	
	@POST
	@Path("/deregister")
	@Consumes(MediaType.APPLICATION_JSON)
	public String deRegisterClient(String credentials){
		
		JSONParser parser = new JSONParser();
		try {
			JSONObject obj = (JSONObject) parser.parse(credentials);
			String uuid = (String) obj.get("uuid");
			String clientpin = (String) obj.get("pin");
			
			AuthenticationDAO authenticationDAO = new AuthenticationDAO();
			String pin = authenticationDAO.getClientCredential(uuid);
			
			if(!clientpin.equals(pin)){
				return "FAILURE";
			}
			else{
				// removing registration document
				EntityDAO entityDAO = new EntityDAO();
				entityDAO.deregisterDevice(uuid);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "SUCCESS";
	}
	
	@POST
	@Path("/addProduct")
	@Consumes(MediaType.APPLICATION_JSON)
	public String addProduct(String productInformation){
		
		JSONParser parser = new JSONParser();
		try {
			JSONObject obj = (JSONObject) parser.parse(productInformation);
			
			String clientUuid = (String) obj.get("uuid");
			String productName = (String) obj.get("name");
			String manufacturer = (String) obj.get("manufacturer");
			Long quantity = (Long) obj.get("quantity");
			
			PaymentDAO paymentDAO = new PaymentDAO();
			
			boolean isRequestCallValid = paymentDAO.checkRequestCapForClient(clientUuid);
			if(!isRequestCallValid){
				return "FAILURE: Request Cap Reached. Please UPGRADE your plan";
			}
			
			ProductDAO productDAO = new ProductDAO();
			productDAO.saveProduct(clientUuid, productName, manufacturer, quantity.intValue());
			
			paymentDAO.updateRequestCount(clientUuid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "SUCCESS";
	}
	
	@POST
	@Path("/updateProductQuantity")
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateProductQuantity(String productInformation){
		
		JSONParser parser = new JSONParser();
		try {
			JSONObject obj = (JSONObject) parser.parse(productInformation);
			
			String clientUuid = (String) obj.get("uuid");
			String productName = (String) obj.get("name");
			Long quantity = (Long) obj.get("quantity");
			
			PaymentDAO paymentDAO = new PaymentDAO();
			
			boolean isRequestCallValid = paymentDAO.checkRequestCapForClient(clientUuid);
			if(!isRequestCallValid){
				return "FAILURE: Request Cap Reached. Please UPGRADE your plan";
			}
			
			ProductDAO productDAO = new ProductDAO();
			productDAO.updateProductQuanity(clientUuid, productName, quantity.intValue());
			
			paymentDAO.updateRequestCount(clientUuid);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return "SUCCESS";
	}
	
	@GET
	@Path("/getPaymentInformation")
	public String getPaymentInformation( @QueryParam("uuid") String uuid){
		
		if(uuid !=null && !uuid.isEmpty()){
			
			PaymentDAO paymentDAO = new PaymentDAO();
			Document paymentDocument = paymentDAO.getPaymentInformation(uuid);
			
			return paymentDocument.toJson();
			
		} else {
			return "FAILURE";
		}
		
	}
	
	@POST
	@Path("/notifyLWM2MServer")
	@Consumes(MediaType.APPLICATION_JSON)
	public String notifyLWM2MServr(String payload){
		
		String clientUuid = null;
		JSONParser parser = new JSONParser();
		try {
			JSONObject obj = (JSONObject) parser.parse(payload);
			JSONObject device = (JSONObject) obj.get("device");
			clientUuid = (String) device.get("uuid");
		}catch(ParseException e){
			e.printStackTrace();
		}
		
		PaymentDAO paymentDAO = new PaymentDAO();
		
		boolean isRequestCallValid = paymentDAO.checkRequestCapForClient(clientUuid);
		if(!isRequestCallValid){
			return "FAILURE: Request Cap Reached. Please upgrade your plan";
		}
		
		System.out.println("========= Notification Received From Vending Machine ============");
		System.out.println(payload);
		System.out.println("=============================================================");
		
		paymentDAO.checkAndAddFeatureAPIType(clientUuid);
		paymentDAO.updateRequestCount(clientUuid);
		
		return "SUCCESS";
	}
	
	@POST
	@Path("/sendDataToServer")
	@Consumes(MediaType.APPLICATION_JSON)
	public String sendDataToServer(String payload){
		
		String clientUuid = null;
		JSONParser parser = new JSONParser();
		try {
			JSONObject obj = (JSONObject) parser.parse(payload);
			clientUuid = (String) obj.get("uuid");
		}catch(ParseException e){
			e.printStackTrace();
		}
		
		PaymentDAO paymentDAO = new PaymentDAO();
		
		boolean isRequestCallValid = paymentDAO.checkRequestCapForClient(clientUuid);
		if(!isRequestCallValid){
			return "FAILURE: Request Cap Reached. Please upgrade your plan";
		}
		
		System.out.println("========= Data Received From Vending Machine ============");
		System.out.println(payload);
		System.out.println("=============================================================");
		
		paymentDAO.updateRequestCount(clientUuid);
		
		return "SUCCESS";
	}
	
	@POST
	@Path("/updatePaymentPlan")
	@Consumes(MediaType.APPLICATION_JSON)
	public String updatePaymentPlan(String payload){
		
		String clientUuid = null;
		String paymentPlan = null;
		JSONParser parser = new JSONParser();
		try {
			JSONObject obj = (JSONObject) parser.parse(payload);
			clientUuid = (String) obj.get("uuid");
			paymentPlan = (String) obj.get("paymentPlan");
		}catch(ParseException e){
			e.printStackTrace();
		}
		
		PaymentDAO paymentDAO = new PaymentDAO();
		paymentDAO.checkAndAddFeatureAPIType(clientUuid);
		paymentDAO.updatePaymentPlan(clientUuid, paymentPlan);
		
		return "SUCCESS";
	}
	
}
