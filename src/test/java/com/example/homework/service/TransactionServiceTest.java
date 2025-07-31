package com.example.homework.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.homework.dto.CustomerDto;
import com.example.homework.dto.TransactionDto;
import com.example.homework.entity.Customer;
import com.example.homework.entity.Transaction;
import com.example.homework.exception.ValidationException;
import com.example.homework.repository.TransactionRepository;
import com.example.homework.utility.RewardCalculationUtility;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

	@Mock
	private TransactionRepository transactionRepository;

	@InjectMocks
	private TransactionService transactionService;

	private Customer getTestCustomer() {

		Customer testCustomer = new Customer();

		testCustomer.setCustomerId(1L);
		testCustomer.setCustomerName("John Doe");
		testCustomer.setEmail("john.doe@example.com");
		testCustomer.setPhone("123-456-7890");

		return testCustomer;
	}

	private Transaction getTransaction() {

		Transaction testTransaction = new Transaction();

		testTransaction.setTransactionId(1L);
		testTransaction.setCustomer(getTestCustomer());
		testTransaction.setBillingPrice(120L);
		testTransaction.setBillingDate(LocalDateTime.now());

		return testTransaction;
	}

	@Test
	void getAllTransactions_Success() {

		Transaction testTransaction = getTransaction();

		when(transactionRepository.findAll()).thenReturn(List.of(testTransaction));

		List<TransactionDto> result = transactionService.getAllTrasactions();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(testTransaction.getTransactionId(), result.get(0).getTransactionId());
		assertEquals(testTransaction.getCustomer().getCustomerId(), result.get(0).getCustomerId());

		verify(transactionRepository, times(1)).findAll();
	}

	@Test
	void getRewardsForCustomers_Success() throws ValidationException {

		Transaction testTransaction = getTransaction();
		Customer testCustomer = getTestCustomer();

		when(transactionRepository.findByCustomerIdAndDateRange(anyString(), any(), any()))
				.thenReturn(List.of(testTransaction));

		List<CustomerDto> result = transactionService.getRewardsForCustomers(1L, 7, 7);

		assertNotNull(result);
		assertEquals(1, result.size());

		CustomerDto customerDto = result.get(0);

		assertEquals(testCustomer.getCustomerId(), customerDto.getCustomerId());
		assertEquals(testCustomer.getCustomerName(), customerDto.getCustomerName());
		assertEquals(90L, customerDto.getTotalRewards());
	}

	@Test
	void getRewardsForCustomers_EmptyTransactions() throws ValidationException {

		when(transactionRepository.findByCustomerIdAndDateRange(anyString(), any(), any()))
				.thenReturn(new ArrayList<>());

		List<CustomerDto> result = transactionService.getRewardsForCustomers(1L, 7, 7);

		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	void getRewardsForAllCustomers_Success() throws ValidationException {

		Transaction testTransaction = getTransaction();
		Customer testCustomer = getTestCustomer();

		when(transactionRepository.findAll()).thenReturn(List.of(testTransaction));

		List<CustomerDto> result = transactionService.getRewardsForAllCustomers();

		assertNotNull(result);
		assertEquals(1, result.size());

		CustomerDto customerDto = result.get(0);

		assertEquals(testCustomer.getCustomerId(), customerDto.getCustomerId());
		assertEquals(90L, customerDto.getTotalRewards());
	}

	@Test
	void calculateRewardForMonth_NullTransactions() {

		assertThrows(ValidationException.class, () -> RewardCalculationUtility.calculateRewardForMonth(null));
	}

	@Test
	void getRewardsForCustomers_MultipleMonths() throws ValidationException {

		Transaction t1 = createTransaction(1L, 120L, LocalDateTime.now());
		Transaction t2 = createTransaction(2L, 75L, LocalDateTime.now().minusMonths(1));

		when(transactionRepository.findByCustomerIdAndDateRange(anyString(), any(), any())).thenReturn(List.of(t1, t2));

		List<CustomerDto> result = transactionService.getRewardsForCustomers(1L, null, null);

		assertNotNull(result);

		assertEquals(1, result.size());

		CustomerDto dto = result.get(0);

		assertEquals(115L, dto.getTotalRewards());

		Map<String, Long> monthlyRewards = dto.getMonthlyRewards();

		assertEquals(2, monthlyRewards.size());
	}

	private Transaction createTransaction(Long id, Long amount, LocalDateTime date) {

		Transaction t = new Transaction();

		t.setTransactionId(id);
		t.setCustomer(getTestCustomer());
		t.setBillingPrice(amount);
		t.setBillingDate(date);

		return t;
	}
}
