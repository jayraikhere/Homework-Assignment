package com.example.homework.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.homework.dto.CustomerDto;
import com.example.homework.dto.TransactionDto;
import com.example.homework.exception.ValidationException;
import com.example.homework.service.TransactionService;

@RestController
@RequestMapping(value = "/api")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	// API to return all transactions
	@GetMapping(value = "/transactions")
	public ResponseEntity<List<TransactionDto>> getAllTransactions() {

		List<TransactionDto> transactions = transactionService.getAllTrasactions();

		return new ResponseEntity<>(transactions, HttpStatus.OK);
	}

	// API to calculate the reward for each customer
	@GetMapping(value = "/rewards/customer/{customerId}")
	public ResponseEntity<List<CustomerDto>> getRewardsForCustomer(@PathVariable Long customerId)
			throws ValidationException {

		if (customerId <= 0) {

			throw new ValidationException("Invalid customerId");

		}

		List<CustomerDto> customers =

				transactionService.getRewardsForCustomers(customerId);

		return new ResponseEntity<List<CustomerDto>>(customers, HttpStatus.OK);
	}
}
