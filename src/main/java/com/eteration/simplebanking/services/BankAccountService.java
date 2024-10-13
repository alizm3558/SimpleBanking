package com.eteration.simplebanking.services;

import com.eteration.simplebanking.model.PhoneBillPaymentTransaction;
import com.eteration.simplebanking.model.TransactionStatus;
import com.eteration.simplebanking.dto.BankAccountDto;
import com.eteration.simplebanking.model.BankAccount;
import com.eteration.simplebanking.exception.InsufficientBalanceException;

import java.util.List;

public interface BankAccountService {
    BankAccount createAccount(String owner, String accountNumber);
    List<BankAccountDto> getAll();
    BankAccount findAccount(String accountNumber);
    TransactionStatus credit(String accountNumber, double amount) throws InsufficientBalanceException;
    BankAccount saveAccount(BankAccount account);
    TransactionStatus debit(String accountNumber, double amount) throws InsufficientBalanceException;
    TransactionStatus phoneBillPayment(String accountNumber, PhoneBillPaymentTransaction transaction) throws InsufficientBalanceException;

    }
