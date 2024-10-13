package com.eteration.simplebanking.services;


import com.eteration.simplebanking.model.TransactionStatus;
import com.eteration.simplebanking.dto.BankAccountDto;
import com.eteration.simplebanking.exception.AccountNotFoundException;
import com.eteration.simplebanking.exception.InsufficientBalanceException;
import com.eteration.simplebanking.model.*;
import com.eteration.simplebanking.repository.BankAccountRepository;
import com.eteration.simplebanking.repository.TransactionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

// This class is a place holder you can change the complete implementation
@Service
public class BankAccountServiceImpl implements BankAccountService {
    @Autowired
    private BankAccountRepository accountRepository;

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private TransactionRepository transactionRepository;

    // Hesap oluşturma ve veritabanına kaydetme
    @Override
    public BankAccount createAccount(String owner, String accountNumber) {
        if (owner == null || owner.trim().isEmpty()) {
            throw new IllegalArgumentException("Hesap sahibi boş olamaz.");
        }

        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Hesap numarası boş olamaz.");
        }

        BankAccount account = new BankAccount(owner, accountNumber);
        return accountRepository.save(account);
    }

    @Override
    public List<BankAccountDto> getAll() {
        return this.accountRepository.findAll().stream()
                .map(account -> this.modelMapper.map(account, BankAccountDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public BankAccount findAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Hesap numarası boş olamaz.");
        }
        Optional<BankAccount> account = accountRepository.findByAccountNumber(accountNumber);
        if(account.isPresent()){
            return account.get();
        }
        throw new AccountNotFoundException("Hesap bulunamadı: " + accountNumber);
    }


    @Override
    public TransactionStatus credit(String accountNumber, double amount) throws InsufficientBalanceException {

        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Hesap numarası boş olamaz.");
        }

        BankAccount account = findAccount(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException("Hesap bulunamadı: " + accountNumber);
        }
        TransactionStatus status = new TransactionStatus("OK", UUID.randomUUID().toString());
        DepositTransaction transaction = new DepositTransaction(amount);
        transaction.setAccount(account); // İşlemi hesaba bağla
        transaction.setApprovalCode(status.getApprovalCode());
        account.post(transaction);

        accountRepository.save(account); // Hesap ve işlemi kaydet
        return status;
    }

    @Override
    public BankAccount saveAccount(BankAccount account) {
        if (account == null) {
            throw new IllegalArgumentException("Hesap nesnesi boş olamaz.");
        }
        return accountRepository.save(account);
    }

    // Hesaptan para çekme işlemi
    @Override
    public TransactionStatus debit(String accountNumber, double amount) throws InsufficientBalanceException {

        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new InsufficientBalanceException("Hesap numarası boş olamaz");
        }

        BankAccount account = findAccount(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException("Hesap bulunamadı: " + accountNumber);
        }


        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException("Yetersiz bakiye");
        }

        TransactionStatus status = new TransactionStatus("OK", UUID.randomUUID().toString());
        WithdrawalTransaction transaction = new WithdrawalTransaction(amount);
        transaction.setAccount(account);
        transaction.setApprovalCode(status.getApprovalCode());
        account.post(transaction);

        accountRepository.save(account);
        return status;
    }

    public TransactionStatus phoneBillPayment(String accountNumber, PhoneBillPaymentTransaction transaction) throws InsufficientBalanceException {

        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new InsufficientBalanceException("Hesap numarası boş olamaz");
        }

        BankAccount account = findAccount(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException("Hesap bulunamadı: " + accountNumber);
        }

        if (account.getBalance() < transaction.getAmount()) {
            throw new InsufficientBalanceException("Yetersiz bakiye");
        }

        TransactionStatus status = new TransactionStatus("OK", UUID.randomUUID().toString());

        transaction.setAccount(account);
        transaction.setApprovalCode(status.getApprovalCode());
        account.post(transaction);

        accountRepository.save(account);

        return  status;
    }

    @Override
    public TransactionStatus transfer(String accountNumber, TransferTransaction transaction) throws InsufficientBalanceException {


        if (accountNumber == null || transaction.getTargetAccountNumber() == null) {
            throw new InsufficientBalanceException("Hesap numaraları boş olamaz");
        }

        BankAccount account = findAccount(accountNumber);

        BankAccount targetAccount = findAccount(transaction.getTargetAccountNumber());

        if (account == null) {
            throw new AccountNotFoundException("Hesap bulunamadı: " + accountNumber);
        }

        if (targetAccount == null) {
            throw new AccountNotFoundException("Gönderilecek hesap bulunamadı: " + transaction.getTargetAccountNumber());
        }

        if (account.getBalance() < transaction.getAmount()) {
            throw new InsufficientBalanceException("Yetersiz bakiye");
        }

        TransactionStatus status = new TransactionStatus("OK", UUID.randomUUID().toString());

        TransferTransaction transferTransaction = new TransferTransaction(accountNumber, transaction.getTargetAccountNumber(), transaction.getAmount());
        transferTransaction.setAccount(account);
        transferTransaction.setApprovalCode(status.getApprovalCode());
        account.post(transferTransaction);

        accountRepository.save(account);

        DepositTransaction depositTransaction = new DepositTransaction(transaction.getAmount());
        depositTransaction.setAccount(targetAccount);
        depositTransaction.setApprovalCode(status.getApprovalCode());
        targetAccount.post(depositTransaction);

        accountRepository.save(targetAccount);

        transactionRepository.save(transferTransaction);

        return status;
    }
}
