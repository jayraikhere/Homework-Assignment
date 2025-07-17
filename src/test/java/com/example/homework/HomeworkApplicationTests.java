package com.example.homework;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.homework.controller.TransactionController;
import com.example.homework.dto.CustomerDto;
import com.example.homework.dto.TransactionDto;
import com.example.homework.entity.Customer;
import com.example.homework.entity.Transaction;
import com.example.homework.exception.ValidationException;
import com.example.homework.repository.TransactionRepository;
import com.example.homework.service.TransactionService;

@SpringBootTest
class HomeworkApplicationTests {

	@Mock
	private TransactionRepository transactionRepository;

	@InjectMocks
	private TransactionService transactionService = new TransactionService();

	@Autowired
	private TransactionController transactionController = new TransactionController();

	@Test
	public void getAllTransactionTest() {

		Transaction transaction = new Transaction();

		transaction.setTransactionId(null);

		transaction.setCustomer(new Customer());

		Mockito.when(transactionRepository.findAll()).thenReturn(List.of(transaction));

		List<TransactionDto> allTransactions = transactionService.getAllTrasactions();

		transactionRepository.findAll().forEach(iterableTransaction -> {

			assertEquals(iterableTransaction.getTransactionId(), allTransactions.get(0).getTransactionId());
		});
	}

	@Test
	public void getRewardForCustomerTest() {

		Long customerId = 2L;

		Customer customer = new Customer();

		customer.setCustomerId(customerId);

		Transaction transaction = new Transaction();

		transaction.setTransactionId(null);

		transaction.setCustomer(customer);

		transaction.setBillingPrice(120L);

		transaction.setBillingDate(LocalDateTime.now());

		Mockito.when(transactionRepository.findByCustomerId(String.valueOf(customerId)))
				.thenReturn(List.of(transaction));

		List<CustomerDto> customerDto = transactionService.getRewardsForCustomers(customerId);

		Long reward = 0L;

		Long price = transaction.getBillingPrice();

		if (price > 100) {

			reward += (price - 100) * 2 + 50 * 1;

		} else

		{

			reward += (price - 50) * 1;
		}

		assertEquals(reward, customerDto.get(0).getFirstMonthRewards());

		assertEquals(reward, customerDto.get(0).getTotalRewards());

	}

	@Test
	public void validationFailureTest() {

		Long customerId = -1L;

		Exception exception = assertThrows(ValidationException.class,
				() -> transactionController.getRewardsForCustomer(customerId));

		assertEquals("Invalid customerId", exception.getMessage());
	}

	@Test
	public void getRewardForAllCustomerTest() {

		Long customerId = 2L;

		Customer customer = new Customer();

		customer.setCustomerId(customerId);

		Transaction transaction = new Transaction();

		transaction.setTransactionId(null);

		transaction.setCustomer(customer);

		transaction.setBillingPrice(120L);

		transaction.setBillingDate(LocalDateTime.now());

		Mockito.when(transactionRepository.findAll()).thenReturn(List.of(transaction));

		List<CustomerDto> customerDto = transactionService.getRewardsForAllCustomers();

		Long reward = 0L;

		Long price = transaction.getBillingPrice();

		if (price > 100) {

			reward += (price - 100) * 2 + 50 * 1;

		} else {

			reward += (price - 50) * 1;
		}

		assertEquals(reward, customerDto.get(0).getFirstMonthRewards());

		assertEquals(reward, customerDto.get(0).getTotalRewards());

	}
}
