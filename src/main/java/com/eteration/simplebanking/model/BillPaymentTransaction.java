package com.eteration.simplebanking.model;

import com.eteration.simplebanking.exception.InsufficientBalanceException;

public class BillPaymentTransaction extends Transaction {
    private String payee;

    public BillPaymentTransaction(String payee, double amount) {
        super(amount);
        this.payee = payee;
    }

    public String getPayee() {
        return payee;
    }

    @Override
    public void apply(BankAccount account) throws InsufficientBalanceException {
        account.debit(amount);
    }

    @Override
    public String toString() {
        return "BillPaymentTransaction{" + "payee='" + payee + '\'' + ", amount=" + amount + '}';
    }
}
