package com.pk.lwm2m.bootstrapserver;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.pk.lwm2m.server.dao.AuthenticationDAO;
import com.pk.lwm2m.server.dao.ServerDAO;
import com.pk.lwm2m.util.BootstrapUtility;

@Path("/bs")
public class LWM2MBootstrapServer {
	
	public static final String serverUuid = "b6043bf8-af16-4d18-aadd-1ffb8ac142f1";
	
	@POST
	@Path("/perform")
	@Consumes(MediaType.APPLICATION_JSON)
	public String performBootstrap(String requestParam)throws ParseException{
		
		JSONParser parser = new JSONParser(); 
		JSONObject requestObj = (JSONObject) parser.parse(requestParam);

		String uuid = (String)requestObj.get("uuid");

		System.out.println(" ============================================ ");
		System.out.println("Bootstrap Initiated for Device... ===> " + uuid);
		System.out.println(" ============================================ ");
		
		ServerDAO serverDAO = new ServerDAO();
		Document serverDocument = serverDAO.getServerInformation(serverUuid);
		
		BootstrapUtility bootstrapUtil = new BootstrapUtility();
		int pin = bootstrapUtil.generatePasswordForClient();
		
		serverDocument.put("password", String.valueOf(pin));
		
		AuthenticationDAO authenticationDAO = new AuthenticationDAO();
		authenticationDAO.saveClientCredentials(uuid, String.valueOf(pin));
		
		return serverDocument.toJson();
		
	}

}
