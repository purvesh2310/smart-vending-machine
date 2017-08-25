package com.pk.lwm2m.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lwm2m_security")
public class LWM2MSecurity {
	
	@Id
	@Column(name = "security_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name = "lwm2m_server_uri")
	private String serverURI;
	
	@Column(name = "bootstrap_server")
	private boolean isBootstrapServer;
	
	@Column(name = "server_identity")
	private String serverIdentity;
	
	@Column(name = "client_identity")
	private String clientIdentity;
	
	@Column(name = "short_server_id")
	private int shortServerId;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getServerURI() {
		return serverURI;
	}
	public void setServerURI(String serverURI) {
		this.serverURI = serverURI;
	}
	public boolean isBootstrapServer() {
		return isBootstrapServer;
	}
	public void setBootstrapServer(boolean isBootstrapServer) {
		this.isBootstrapServer = isBootstrapServer;
	}
	public String getServerIdentity() {
		return serverIdentity;
	}
	public void setServerIdentity(String serverIdentity) {
		this.serverIdentity = serverIdentity;
	}
	public String getClientIdentity() {
		return clientIdentity;
	}
	public void setClientIdentity(String clientIdentity) {
		this.clientIdentity = clientIdentity;
	}
	public int getShortServerId() {
		return shortServerId;
	}
	public void setShortServerId(int shortServerId) {
		this.shortServerId = shortServerId;
	}

}
