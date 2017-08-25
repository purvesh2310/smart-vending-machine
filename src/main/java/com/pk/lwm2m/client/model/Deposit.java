package com.pk.lwm2m.client.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "deposit")
public class Deposit {
	
	@Id
	@Column(name = "deposit_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@OneToOne(fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
	@JoinColumn(name="transaction_id")
	private Transaction transaction;
	
	@Column(name = "one_unit")
	private int oneUnit;
	
	@Column(name = "five_unit")
	private int fiveUnit;
	
	@Column(name = "ten_unit")
	private int tenUnit;
	
	@Column(name = "twenty_unit")
	private int twentyUnit;
	
	@Column(name = "hundred_unit")
	private int hundredUnit;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public int getOneUnit() {
		return oneUnit;
	}

	public void setOneUnit(int oneUnit) {
		this.oneUnit = oneUnit;
	}

	public int getFiveUnit() {
		return fiveUnit;
	}

	public void setFiveUnit(int fiveUnit) {
		this.fiveUnit = fiveUnit;
	}

	public int getTenUnit() {
		return tenUnit;
	}

	public void setTenUnit(int tenUnit) {
		this.tenUnit = tenUnit;
	}

	public int getTwentyUnit() {
		return twentyUnit;
	}

	public void setTwentyUnit(int twentyUnit) {
		this.twentyUnit = twentyUnit;
	}

	public int getHundredUnit() {
		return hundredUnit;
	}

	public void setHundredUnit(int hundredUnit) {
		this.hundredUnit = hundredUnit;
	}

}
