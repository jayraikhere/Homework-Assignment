package com.example.homework.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public class TransactionDto {

	@NotNull(message = "validation.transaction.id.null")
	private Long transactionId;

	@NotNull(message = "validation.transaction.customer.null")
	@Min(value = 1, message = "validation.customer.id.min")
	private Long customerId;

	@NotNull(message = "validation.transaction.price.null")
	@Min(value = 0, message = "validation.transaction.price.min")
	private Long billingPrice;

	@NotNull(message = "validation.transaction.date.null")
	@PastOrPresent(message = "validation.transaction.date.future")
	private LocalDateTime billingDate;

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
