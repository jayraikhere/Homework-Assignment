package com.example.homework.dto;

import java.util.Map;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CustomerDto {

	@NotBlank(message = "validation.customer.name.blank")
	private String customerName;

	@NotNull(message = "validation.reward.total.null")
	@Min(value = 0, message = "validation.reward.total.min")
	private Long totalRewards;

	@NotNull(message = "validation.reward.monthly.null")
	private Map<String, Long> monthlyRewards;

	@NotNull(message = "validation.customer.id.null")
	@Min(value = 1, message = "validation.customer.id.min")
	private Long customerId;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Long getTotalRewards() {
		return totalRewards;
	}

	public void setTotalRewards(Long totalRewards) {
		this.totalRewards = totalRewards;
	}

	public Map<String, Long> getMonthlyRewards() {
		return monthlyRewards;
	}

	public void setMonthlyRewards(Map<String, Long> monthlyRewards) {
		this.monthlyRewards = monthlyRewards;
	}

}
