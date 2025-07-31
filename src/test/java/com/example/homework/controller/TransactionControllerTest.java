package com.example.homework.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.homework.dto.CustomerDto;
import com.example.homework.dto.TransactionDto;
import com.example.homework.service.TransactionService;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TransactionService transactionService;

	private CustomerDto testCustomerDto;

	private TransactionDto testTransactionDto;

	@BeforeEach
	void setUp() {
		testCustomerDto = new CustomerDto();
		testCustomerDto.setCustomerId(1L);
		testCustomerDto.setCustomerName("John Doe");
		testCustomerDto.setTotalRewards(90L);
		Map<String, Long> monthlyRewards = new HashMap<>();
		monthlyRewards.put("JULY", 90L);
		testCustomerDto.setMonthlyRewards(monthlyRewards);

		testTransactionDto = new TransactionDto();
		testTransactionDto.setTransactionId(1L);
		testTransactionDto.setCustomerId(1L);
		testTransactionDto.setBillingPrice(120L);
	}

	@Test
	void getAllTransactions_Success() throws Exception {

		when(transactionService.getAllTrasactions()).thenReturn(Arrays.asList(testTransactionDto));

		MvcResult result = mockMvc.perform(get("/api/transactions")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

		String content = result.getResponse().getContentAsString();
		assertTrue(content.contains("Transactions retrieved successfully"));
		assertTrue(content.contains(testTransactionDto.getTransactionId().toString()));
	}

	@Test
	void getRewardsForCustomer_Success() throws Exception {

		when(transactionService.getRewardsForCustomers(1L, 7, 7)).thenReturn(Arrays.asList(testCustomerDto));

		mockMvc.perform(get("/api/rewards/customer/1").param("fromMonth", "7").param("toMonth", "7"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.data[0].customerId").value(1))
				.andExpect(jsonPath("$.data[0].totalRewards").value(90));
	}

	@Test
	void getRewardsForCustomer_InvalidCustomerId() throws Exception {

		mockMvc.perform(get("/api/rewards/customer/-1")).andExpect(status().isBadRequest());
	}

	@Test
	void getRewardsForCustomer_InvalidFromMonth() throws Exception {

		mockMvc.perform(get("/api/rewards/customer/1").param("fromMonth", "13")).andExpect(status().isBadRequest());
	}

	@Test
	void getRewardsForCustomer_InvalidToMonth() throws Exception {

		mockMvc.perform(get("/api/rewards/customer/1").param("toMonth", "0")).andExpect(status().isBadRequest());
	}

	@Test
	void getRewardsForCustomer_FromMonthGreaterThanToMonth() throws Exception {

		mockMvc.perform(get("/api/rewards/customer/1").param("fromMonth", "7").param("toMonth", "6"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void getRewardsForCustomer_ServiceThrowsException() throws Exception {

		when(transactionService.getRewardsForCustomers(anyLong(), any(), any()))
				.thenThrow(new RuntimeException("Runtime Exception"));

		mockMvc.perform(get("/api/rewards/customer/1")).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.message").value("Something went wrong. " + "Runtime Exception"));
	}

	@Test
	void getRewardsForCustomer_NoMonthParams() throws Exception {

		when(transactionService.getRewardsForCustomers(1L, null, null)).thenReturn(Arrays.asList(testCustomerDto));

		mockMvc.perform(get("/api/rewards/customer/1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.data[0].customerId").value(1));
	}

	@Test
	void getRewardsForCustomer_OnlyFromMonth() throws Exception {

		when(transactionService.getRewardsForCustomers(1L, 7, null)).thenReturn(Arrays.asList(testCustomerDto));

		mockMvc.perform(get("/api/rewards/customer/1").param("fromMonth", "7")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.data[0].customerId").value(1));
	}

	@Test
	void getRewardsForCustomer_OnlyToMonth() throws Exception {

		when(transactionService.getRewardsForCustomers(1L, null, 7)).thenReturn(Arrays.asList(testCustomerDto));

		mockMvc.perform(get("/api/rewards/customer/1").param("toMonth", "7")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.data[0].customerId").value(1));
	}
}
