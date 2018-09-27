package com.homedirect.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import com.homedirect.entity.Transaction;

@Repository
public interface TransactionRepository
		extends JpaRepository<Transaction, Integer>, QuerydslPredicateExecutor<Transaction> {
	
	List<Transaction> findByFromAccount(String accountNumber, Pageable pageable);
}
