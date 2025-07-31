package com.example.homework.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.homework.entity.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    @Query("SELECT t FROM Transaction t WHERE t.customer.customerId = :customerId " +
           "AND (:fromMonth IS NULL OR FUNCTION('MONTH', t.billingDate) >= :fromMonth) " +
           "AND (:toMonth IS NULL OR FUNCTION('MONTH', t.billingDate) <= :toMonth)")
    List<Transaction> findByCustomerIdAndDateRange(String customerId, Integer fromMonth, Integer toMonth);
}
