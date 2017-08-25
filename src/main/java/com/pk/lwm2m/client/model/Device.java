package com.pk.lwm2m.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.pk.lwm2m.listener.EntityListener;

/**
 * @author Purvesh
 *
 */
@Entity
@Table(name = "device")
public class Device {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "uuid")
	private String uuid;
	
	@Column(name = "manufacturer")
	private String manufacturer;
	
	@Column(name = "device_type")
	private String deviceType;
	
	@Column(name = "model_number")
	private String modelNumber;
	
	@Column(name = "serial_number")
	private String serialNumber;
	
	@Column(name = "isRegistered")
	private boolean isRegistered;
	
	@Column(name="payment_plan")
	private String paymentPlan;
	
	@Column(name="power_source_voltage")
	private int powerSourceVoltage;
	
	@Column(name="power_source_current")
	private int powerSourceCurrent;
	
	@Column(name="isDeviceOnline")
	private int isDeviceOnline;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="security_id")
	private LWM2MSecurity security;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getModelNumber() {
		return modelNumber;
	}
	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public boolean isRegistered() {
		return isRegistered;
	}
	public void setRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}
	public LWM2MSecurity getSecurity() {
		return security;
	}
	public void setSecurity(LWM2MSecurity security) {
		this.security = security;
	}
	public String getPaymentPlan() {
		return paymentPlan;
	}
	public void setPaymentPlan(String paymentPlan) {
		this.paymentPlan = paymentPlan;
	}
	public int getPowerSourceVoltage() {
		return powerSourceVoltage;
	}
	public void setPowerSourceVoltage(int powerSourceVoltage) {
		this.powerSourceVoltage = powerSourceVoltage;
	}
	public int getPowerSourceCurrent() {
		return powerSourceCurrent;
	}
	public void setPowerSourceCurrent(int powerSourceCurrent) {
		this.powerSourceCurrent = powerSourceCurrent;
	}
	public int getIsDeviceOnline() {
		return isDeviceOnline;
	}
	public void setIsDeviceOnline(int isDeviceOnline) {
		this.isDeviceOnline = isDeviceOnline;
	}
}
