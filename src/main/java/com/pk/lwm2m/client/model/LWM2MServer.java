package com.pk.lwm2m.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lwm2m_server")
public class LWM2MServer {
	
	@Id
	@Column(name = "short_server_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int shortServerId;
	
	@Column(name = "lifetime")
	private long lifetime;

	public int getShortServerId() {
		return shortServerId;
	}
	public void setShortServerId(int shortServerId) {
		this.shortServerId = shortServerId;
	}
	public long getLifetime() {
		return lifetime;
	}
	public void setLifetime(long lifetime) {
		this.lifetime = lifetime;
	}
}
