package com.example.homework.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Transaction {

	@Id
	Long transactionId;

	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	Customer customer;

	Long billingPrice;

	LocalDateTime billingDate;

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Long getBillingPrice() {
		return billingPrice;
	}

	public void setBillingPrice(Long billingPrice) {
		this.billingPrice = billingPrice;
	}

	public LocalDateTime getBillingDate() {
		return billingDate;
	}

	public void setBillingDate(LocalDateTime billingDate) {
		this.billingDate = billingDate;
	}

}
