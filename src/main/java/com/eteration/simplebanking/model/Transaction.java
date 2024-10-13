package com.eteration.simplebanking.model;

import com.eteration.simplebanking.exception.InsufficientBalanceException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;

// This class is a place holder you can change the complete implementation
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "transaction_type")
public abstract class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected double amount;
    protected Date date;

    @ManyToOne
    @JoinColumn(name = "account_number")
    @JsonIgnore
    private BankAccount account;

    @JsonProperty("type")// işlem türü için
    public String getTransactionType() {
        return this.getClass().getSimpleName();
    }
    public Transaction() {
        this.date = new Date();
    }

    public Transaction(double amount) {
        this();
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public BankAccount getAccount() {
        return account;
    }

    public void setAccount(BankAccount account) {
        this.account = account;
    }

    public abstract void apply(BankAccount account) throws InsufficientBalanceException;

    @Override
    public String toString() {
        return "Transaction{" + "date=" + date + ", amount=" + amount + '}';
    }
}
