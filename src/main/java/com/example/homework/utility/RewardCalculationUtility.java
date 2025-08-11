package com.example.homework.utility;

import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.homework.entity.Transaction;
import com.example.homework.exception.ValidationException;

public class RewardCalculationUtility {

	private static final Logger LOG = LogManager.getLogger(RewardCalculationUtility.class);

	// Constants for reward calculation thresholds and multipliers
	private static final long TIER2_THRESHOLD = 100L;
	private static final long TIER1_THRESHOLD = 50L;
	private static final long TIER2_MULTIPLIER = 2L;
	private static final long TIER1_MULTIPLIER = 1L;

	public static MonthlyRewardRecord calculateRewardForMonth(List<Transaction> transactions)
			throws ValidationException {

		if (transactions == null) {
			throw new ValidationException("transaction cannot be null");
		}

		Map<Month, Long> monthlyTotalMap = new HashMap<>();

		// First, sum up all transactions by month
		transactions.forEach(transaction -> {

			Month month = transaction.getBillingDate() != null ? transaction.getBillingDate().getMonth() : null;

			if(month != null){

				monthlyTotalMap.merge(month, transaction.getBillingPrice(), Long::sum);
			}
		});

		// Calculate rewards for each month
		Map<String, Long> monthlyRewards = new HashMap<>();

		for (Map.Entry<Month, Long> entry : monthlyTotalMap.entrySet()) {
			try {

				monthlyRewards.put(entry.getKey().toString(),
						RewardCalculationUtility.calculateReward(entry.getValue()));

			} catch (ValidationException e) {
				LOG.error("Error calculating rewards for month {}: {}", entry.getKey(), e.getMessage());
			}
		}

		return new MonthlyRewardRecord(monthlyRewards);
	}

	/**
	 * Calculates reward points based on the transaction amount. Rules: - Purchases
	 * over $100 earn 2x points on amount above $100, plus 1x points for amount
	 * between $50-$100 - Purchases between $50-$100 earn 1x points on amount above
	 * $50 - Purchases under $50 earn no points
	 * 
	 * @param price Transaction amount
	 * @return Calculated reward points
	 * @throws ValidationException if price is null or negative
	 */
	public static Long calculateReward(Long price) throws ValidationException {
		// Input validation
		if (price == null) {
			throw new ValidationException("Transaction amount cannot be null");
		}
		if (price < 0) {
			throw new ValidationException("Transaction amount cannot be negative");
		}

		long reward = 0L;

		// No rewards for purchases under $50
		if (price <= TIER1_THRESHOLD) {
			return 0L;
		}

		// Calculate tier 2 rewards (2x points for amount over $100)
		if (price > TIER2_THRESHOLD) {
			reward += (price - TIER2_THRESHOLD) * TIER2_MULTIPLIER;
			// Add tier 1 rewards for the amount between $50 and $100
			reward += (TIER2_THRESHOLD - TIER1_THRESHOLD) * TIER1_MULTIPLIER;
		} else {
			// Calculate tier 1 rewards (1x points for amount between $50 and $100)
			reward += (price - TIER1_THRESHOLD) * TIER1_MULTIPLIER;
		}

		return reward;
	}

	public static final class MonthlyRewardRecord {

		private final Map<String, Long> monthlyRewards;
		private final Long totalReward;

		public MonthlyRewardRecord(Map<String, Long> monthlyRewards) {
			this.monthlyRewards = monthlyRewards;
			this.totalReward = monthlyRewards.values().stream().mapToLong(Long::valueOf).sum();
		}

		public Map<String, Long> getMonthlyRewards() {
			return monthlyRewards;
		}

		public Long getTotalReward() {
			return totalReward;
		}
	}

}
