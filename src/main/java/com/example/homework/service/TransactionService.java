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
import com.example.homework.repository.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	private Logger LOG = LogManager.getLogger(TransactionService.class);

	// method to get all transactions
	public List<TransactionDto> getAllTrasactions() {

		Iterable<Transaction> dbTransactions = transactionRepository.findAll();

		return mapTransactions(dbTransactions);
	}

	// Method to map Transaction object to TransactionDto object
	private List<TransactionDto> mapTransactions(Iterable<Transaction> dbTransactions) {

		List<TransactionDto> transactions = new ArrayList<>();

		dbTransactions.forEach(transaction -> {

			TransactionDto transactionDto = new TransactionDto();

			transactionDto.setTransactionId(transaction.getTransactionId());

			transactionDto.setCustomerId(transaction.getCustomer().getCustomerId());

			transactionDto.setBillingPrice(transaction.getBillingPrice());

			transactionDto.setBillingDate(transaction.getBillingDate());

			transactions.add(transactionDto);

		});

		return transactions;

	}

	public List<CustomerDto> getRewardsForCustomers(Long customerId) {

		LOG.info("Fetching all transactions of customerId: {}", customerId);

		List<Transaction> transactions = transactionRepository.findByCustomerId(String.valueOf(customerId));

		List<CustomerDto> customers = new ArrayList<CustomerDto>();

		MonthlyRewardRecord monthlyRewardRecord = calculateRewardForMonth(transactions);

		Long totalReward = monthlyRewardRecord.firstMonthReward() + monthlyRewardRecord.secondMonthReward()
				+ monthlyRewardRecord.thirdMonthReward();

		CustomerDto customerDto = new CustomerDto();

		customerDto.setCustomerId(Long.valueOf(customerId));

		customerDto.setCustomerName(transactions.get(0).getCustomer().getCustomerName());

		customerDto.setTotalRewards(totalReward);

		customerDto.setFirstMonthRewards(monthlyRewardRecord.firstMonthReward());

		customerDto.setSecondMonthRewards(monthlyRewardRecord.secondMonthReward());

		customerDto.setThirdMonthRewards(monthlyRewardRecord.thirdMonthReward());

		customers.add(customerDto);

		return customers;
	}

	private MonthlyRewardRecord calculateRewardForMonth(List<Transaction> transactions) {

		Long firstMonthReward = 0L;

		Long secondMonthReward = 0L;

		Long thirdMonthReward = 0L;

		Map<Month, Long> monthlyRewardMap = new HashMap<Month, Long>();

		transactions.forEach(transaction -> {

			Month month = transaction.getBillingDate().getMonth();

			if (monthlyRewardMap.containsKey(month)) {

				monthlyRewardMap.put(month, monthlyRewardMap.get(month) + transaction.getBillingPrice());

			} else {

				monthlyRewardMap.put(month, transaction.getBillingPrice());

			}

		});

		for (Month entry : monthlyRewardMap.keySet()) {

			if (firstMonthReward.equals(0L)) {

				firstMonthReward = calculateReward(monthlyRewardMap.get(entry));

			} else if (secondMonthReward.equals(0L))

			{

				secondMonthReward = calculateReward(monthlyRewardMap.get(entry));

			} else {

				thirdMonthReward = calculateReward(monthlyRewardMap.get(entry));

			}
		}

		return new MonthlyRewardRecord(firstMonthReward, secondMonthReward, thirdMonthReward);
	}

	public List<CustomerDto> getRewardsForAllCustomers() {

		Iterable<Transaction> transactions = transactionRepository.findAll();

		Map<Long, List<Transaction>> transactionsByCustomerIdMap = new HashMap<Long, List<Transaction>>();

		transactions.forEach(transaction -> transactionsByCustomerIdMap
				.computeIfAbsent(transaction.getCustomer().getCustomerId(), value -> new ArrayList<Transaction>())
				.add(transaction));

		List<CustomerDto> customers = new ArrayList<CustomerDto>();

		for (Entry<Long, List<Transaction>> transaction : transactionsByCustomerIdMap.entrySet()) {

			MonthlyRewardRecord monthlyRewardRecord = calculateRewardForMonth(transaction.getValue());

			Long totalReward = monthlyRewardRecord.firstMonthReward() + monthlyRewardRecord.secondMonthReward()
					+ monthlyRewardRecord.thirdMonthReward();

			CustomerDto customerDto = new CustomerDto();

			customerDto.setCustomerId(transaction.getKey());

			customerDto.setCustomerName(transaction.getValue().get(0).getCustomer().getCustomerName());

			customerDto.setTotalRewards(totalReward);

			customerDto.setFirstMonthRewards(monthlyRewardRecord.firstMonthReward());

			customerDto.setSecondMonthRewards(monthlyRewardRecord.secondMonthReward());

			customerDto.setThirdMonthRewards(monthlyRewardRecord.thirdMonthReward());

			customers.add(customerDto);

		}

		return customers;

	}

	private Long calculateReward(Long price) {

		Long reward = 0L;

		if (price > 100) {

			reward += (price - 100) * 2 + 50 * 1;

		} else {

			reward += (price - 50) * 1;

		}

		return reward;

	}

}s

final record MonthlyRewardRecord(Long firstMonthReward, Long secondMonthReward, Long thirdMonthReward) {
}