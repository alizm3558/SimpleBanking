package com.eteration.simplebanking.repository;

import com.eteration.simplebanking.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

    Optional<BankAccount> findByAccountNumber(String accountNumber);

}
