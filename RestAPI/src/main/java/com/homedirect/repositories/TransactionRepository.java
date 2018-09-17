package com.homedirect.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.homedirect.entity.TransactionHistory;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionHistory, Integer> {

	List<TransactionHistory> findByFromAccount(String accountNumber);
	
	List<TransactionHistory> findByType(Byte type);
	
	List<TransactionHistory> findByFromAccountAndType(String accountNumber, Byte type);
}
