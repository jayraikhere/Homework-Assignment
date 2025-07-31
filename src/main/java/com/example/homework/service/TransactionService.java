package com.example.homework.service;

import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.homework.dto.CustomerDto;
import com.example.homework.dto.TransactionDto;
import com.example.homework.entity.Transaction;
import com.example.homework.exception.ValidationException;
import com.example.homework.repository.TransactionRepository;
import com.example.homework.utility.RewardCalculationUtility;
import com.example.homework.utility.RewardCalculationUtility.MonthlyRewardRecord;
import com.example.homework.utility.TransactionMapper;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	private Logger LOG = LogManager.getLogger(TransactionService.class);

	// method to get all transactions
	public List<TransactionDto> getAllTrasactions() {

		Iterable<Transaction> dbTransactions = transactionRepository.findAll();

		return TransactionMapper.mapToTransactionDtos(dbTransactions);
	}

	public List<CustomerDto> getRewardsForCustomers(Long customerId, Integer fromMonth, Integer toMonth)
			throws ValidationException {

		LOG.info("Fetching transactions for customerId: {}, fromMonth: {}, toMonth: {}", customerId, fromMonth,
				toMonth);

		List<Transaction> transactions = transactionRepository.findByCustomerIdAndDateRange(String.valueOf(customerId),
				fromMonth, toMonth);

		List<CustomerDto> customers = new ArrayList<CustomerDto>();

		if (transactions.isEmpty()) {
			return customers;
		}

		MonthlyRewardRecord monthlyRewardRecord = calculateRewardForMonth(transactions);

		CustomerDto customerDto = new CustomerDto();
		customerDto.setCustomerId(Long.valueOf(customerId));
		customerDto.setCustomerName(transactions.get(0).getCustomer().getCustomerName());
		customerDto.setTotalRewards(monthlyRewardRecord.getTotalReward());
		customerDto.setMonthlyRewards(monthlyRewardRecord.getMonthlyRewards());

		customers.add(customerDto);

		return customers;
	}

	private MonthlyRewardRecord calculateRewardForMonth(List<Transaction> transactions) throws ValidationException {

		if (transactions == null) {
			throw new ValidationException("Transactions list cannot be null");
		}

		Map<Month, Long> monthlyTotalMap = new HashMap<>();

		// First, sum up all transactions by month
		for (Transaction transaction : transactions) {
			if (transaction == null) {
				throw new ValidationException("Transaction cannot be null");
			}
			if (transaction.getBillingDate() == null) {
				throw new ValidationException("Transaction billing date cannot be null");
			}
			if (transaction.getBillingPrice() == null) {
				throw new ValidationException("Transaction billing price cannot be null");
			}

			Month month = transaction.getBillingDate().getMonth();

			monthlyTotalMap.merge(month, transaction.getBillingPrice(), Long::sum);
		}

		// Calculate rewards for each month
		Map<String, Long> monthlyRewards = new HashMap<>();

		for (Map.Entry<Month, Long> entry : monthlyTotalMap.entrySet()) {
			try {

				monthlyRewards.put(entry.getKey().toString(),
						RewardCalculationUtility.calculateReward(entry.getValue()));

			} catch (ValidationException e) {
				// Log the error and skip invalid entries
				LOG.error("Error calculating rewards for month {}: {}", entry.getKey(), e.getMessage());
			}
		}

		return new MonthlyRewardRecord(monthlyRewards);
	}

	public List<CustomerDto> getRewardsForAllCustomers() throws ValidationException {

		if (transactionRepository == null) {
			throw new ValidationException("Transaction repository is not initialized");
		}

		Iterable<Transaction> transactions = transactionRepository.findAll();

		Map<Long, List<Transaction>> transactionsByCustomerIdMap = new HashMap<Long, List<Transaction>>();

		transactions.forEach(transaction -> transactionsByCustomerIdMap
				.computeIfAbsent(transaction.getCustomer().getCustomerId(), value -> new ArrayList<Transaction>())
				.add(transaction));

		List<CustomerDto> customers = new ArrayList<CustomerDto>();

		for (Entry<Long, List<Transaction>> transaction : transactionsByCustomerIdMap.entrySet()) {
			MonthlyRewardRecord monthlyRewardRecord = calculateRewardForMonth(transaction.getValue());

			CustomerDto customerDto = new CustomerDto();
			customerDto.setCustomerId(transaction.getKey());
			customerDto.setCustomerName(transaction.getValue().get(0).getCustomer().getCustomerName());
			customerDto.setTotalRewards(monthlyRewardRecord.getTotalReward());
			customerDto.setMonthlyRewards(monthlyRewardRecord.getMonthlyRewards());

			customers.add(customerDto);

		}

		return customers;

	}

}