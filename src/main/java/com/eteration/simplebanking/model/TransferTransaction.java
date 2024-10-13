package com.eteration.simplebanking.model;

import com.eteration.simplebanking.exception.InsufficientBalanceException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("TRANSFER")
public class TransferTransaction extends Transaction{

    private String targetAccountNumber;
    
    private String approvalCode;

    public TransferTransaction() {

    }
    public TransferTransaction(String targetAccountNumber, String approvalCode, double amount) {
        super(amount);
        this.targetAccountNumber = targetAccountNumber;
        this.approvalCode = approvalCode;
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public void setTargetAccountNumber(String targetAccountNumber) {
        this.targetAccountNumber = targetAccountNumber;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    @Override
    public void apply(BankAccount account) throws InsufficientBalanceException {
        account.setBalance(account.getBalance() - getAmount());
    }

    @Override
    public String toString() {
        return "TransferTransaction{" +
                "targerAccountNumber='" + targetAccountNumber + '\'' +
                ", approvalCode='" + approvalCode + '\'' +
                '}';
    }
}
