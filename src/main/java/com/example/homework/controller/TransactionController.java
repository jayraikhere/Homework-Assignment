package com.example.homework.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.homework.dto.ApiResponse;
import com.example.homework.dto.CustomerDto;
import com.example.homework.dto.TransactionDto;
import com.example.homework.exception.ValidationException;
import com.example.homework.service.TransactionService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping(value = "/api")
@Validated
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	// API to return all transactions
	@GetMapping(value = "/transactions")
	public ResponseEntity<ApiResponse<List<TransactionDto>>> getAllTransactions() {

		List<TransactionDto> transactions = transactionService.getAllTrasactions();

		ApiResponse<List<TransactionDto>> response = ApiResponse.success(transactions,
				"Transactions retrieved successfully");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// API to calculate the reward for each customer
	@GetMapping(value = "/rewards/customer/{customerId}")
	public ResponseEntity<ApiResponse<List<CustomerDto>>> getRewardsForCustomer(
			@PathVariable @Min(value = 1, message = "validation.customer.id.min") Long customerId,
			@RequestParam(required = false) @Min(value = 1, message = "validation.month.range") @Max(value = 12, message = "validation.month.range") Integer fromMonth,
			@RequestParam(required = false) @Min(value = 1, message = "validation.month.range") @Max(value = 12, message = "validation.month.range") Integer toMonth)
			throws ValidationException {

		if (fromMonth != null && toMonth != null && fromMonth > toMonth) {
			throw new ValidationException("validation.month.from.greater");
		}

		List<CustomerDto> customers = transactionService.getRewardsForCustomers(customerId, fromMonth, toMonth);

		String message = String.format("Rewards retrieved successfully for customer %d", customerId);

		ApiResponse<List<CustomerDto>> response = ApiResponse.success(customers, message);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
