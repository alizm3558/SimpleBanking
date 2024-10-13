package com.eteration.simplebanking.model;


import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

// This class is a place holder you can change the complete implementation
@Entity
@DiscriminatorValue("DEPOSIT")
public class DepositTransaction extends Transaction {

    private String approvalCode;
    public DepositTransaction() {
        super();
    }

    public DepositTransaction(double amount) {
        super(amount);
    }

    @Override
    public void apply(BankAccount account) {
        account.credit(amount);
    }

    @Override
    public String toString() {
        return "DepositTransaction{" + "amount=" + amount + '}';
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }
}