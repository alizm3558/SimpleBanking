package com.eteration.simplebanking.model;


import com.eteration.simplebanking.exception.InsufficientBalanceException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

// This class is a place holder you can change the complete implementation
@Entity
@DiscriminatorValue("WITHDRAWAL")
public class WithdrawalTransaction extends Transaction {

    private String approvalCode;
    public WithdrawalTransaction() {
        super();
    }

    public WithdrawalTransaction(double amount) {
        super(amount);
    }

    @Override
    public void apply(BankAccount account) throws InsufficientBalanceException {
        account.debit(amount);
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    @Override
    public String toString() {
        return "WithdrawalTransaction{" + "amount=" + amount + '}';
    }
}


