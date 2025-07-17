package com.example.homework.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.homework.entity.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

	@Query("SELECT t FROM Transaction t WHERE t.customer.customerId =:customerId")
	List<Transaction> findByCustomerId(String customerId);
}
