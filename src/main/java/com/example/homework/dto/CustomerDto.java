package com.example.homework.dto;

public class CustomerDto {

	String customerName;
	Long totalRewards;
	Long firstMonthRewards;
	Long secondMonthRewards;
	Long thirdMonthRewards;
	Long customerId;

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

	public Long getFirstMonthRewards() {
		return firstMonthRewards;
	}

	public void setFirstMonthRewards(Long firstMonthRewards) {
		this.firstMonthRewards = firstMonthRewards;
	}

	public Long getSecondMonthRewards() {
		return secondMonthRewards;
	}

	public void setSecondMonthRewards(Long secondMonthRewards) {
		this.secondMonthRewards = secondMonthRewards;
	}

	public Long getThirdMonthRewards() {
		return thirdMonthRewards;
	}

	public void setThirdMonthRewards(Long thirdMonthRewards) {
		this.thirdMonthRewards = thirdMonthRewards;
	}

}
