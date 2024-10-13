package com.eteration.simplebanking.model;


import com.eteration.simplebanking.exception.InsufficientBalanceException;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Data
@Entity
public class BankAccount {

    @Id
    private String accountNumber;

    private String owner;
    private double balance;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "account")
    private List<Transaction> transactions = new ArrayList<>();

    public BankAccount() {

    }

    public BankAccount(String owner, String accountNumber) {
        this.owner = owner;
        this.accountNumber = accountNumber;
        this.balance = 0.0;
    }

    // Hesaba para yatırma
    public void credit(double amount) {
        this.balance += amount;
    }


    public void debit(double amount) throws InsufficientBalanceException {
        if (this.balance < amount) {
            throw new InsufficientBalanceException("Yetersiz bakiye");
        }
        this.balance -= amount;
    }
    public void deposit(double amount) {
        this.balance += amount;
    }
    // İşlem gerçekleştirmek için
    public void post(Transaction transaction) throws InsufficientBalanceException{
        transaction.apply(this);
        this.transactions.add(transaction);
    }

    public void withdraw(double amount) throws InsufficientBalanceException {
        debit(amount);  // debit metodunu kullanarak bakiyeyi azaltıyoruz
    }
}
