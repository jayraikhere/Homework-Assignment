package com.example.homework.utility;

import java.util.ArrayList;
import java.util.List;

import com.example.homework.dto.CustomerDto;
import com.example.homework.dto.TransactionDto;
import com.example.homework.entity.Transaction;
import com.example.homework.utility.RewardCalculationUtility.MonthlyRewardRecord;

public class TransactionMapper {

	public static List<TransactionDto> mapToTransactionDtos(Iterable<Transaction> transactions) {
		List<TransactionDto> transactionDtos = new ArrayList<>();

		transactions.forEach(transaction -> {

			TransactionDto dto = new TransactionDto();

			dto.setTransactionId(transaction.getTransactionId());
			dto.setCustomerId(transaction.getCustomer().getCustomerId());
			dto.setBillingPrice(transaction.getBillingPrice());
			dto.setBillingDate(transaction.getBillingDate());

			transactionDtos.add(dto);
		});

		return transactionDtos;
	}

	public static CustomerDto createCustomerDto(Long customerId, String customerName,
			MonthlyRewardRecord rewardRecord) {

		CustomerDto customerDto = new CustomerDto();
		customerDto.setCustomerId(customerId);
		customerDto.setCustomerName(customerName);
		customerDto.setTotalRewards(rewardRecord.getTotalReward());
		customerDto.setMonthlyRewards(rewardRecord.getMonthlyRewards());
		return customerDto;
	}
}
