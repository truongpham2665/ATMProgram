package com.homedirect.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.homedirect.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>, QuerydslPredicateExecutor<Account> {

	Optional<Account> findById(Integer id);
	
	List<Account> findByUsernameContaining(String username, Pageable pageable);
	
	Account findByUsername(String name);

	Account findByAccountNumber(String accountNumber);

	Account findByUsernameAndPassword(String username, String password);

	Account findByUsernameAndAccountNumber(String username, String accountNumber);
<<<<<<< HEAD

	List<Account> findAll();
=======
>>>>>>> 5f832f4e54677272a5cef9273264cd5453d0c2bc
}
