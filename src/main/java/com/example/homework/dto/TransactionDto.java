package com.example.homework.dto;

import java.time.LocalDateTime;

public class TransactionDto {

	Long transactionId;
	Long customerId;
	Long billingPrice;
	LocalDateTime billingDate;

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
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
