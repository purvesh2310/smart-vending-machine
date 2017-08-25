package com.pk.lwm2m.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "notification_status")
public class NotificationStatus {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "object_id")
	private String objectId;
	
	@Column(name = "notify_status")
	private boolean notifyStatus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public boolean isNotifyStatus() {
		return notifyStatus;
	}

	public void setNotifyStatus(boolean notifyStatus) {
		this.notifyStatus = notifyStatus;
	}
}
