package com.eteration.simplebanking.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PHONEBILLPAYMENT")
public class PhoneBillPaymentTransaction extends Transaction {

    private String operator;
    private String phoneNumber;

    private String approvalCode;
    public PhoneBillPaymentTransaction() {
        super();
    }
    // Constructor
    public PhoneBillPaymentTransaction(String operator, String phoneNumber, double amount) {
        super(amount); // Transaction'ın amount özelliğini ayarlamak
        this.operator = operator;
        this.phoneNumber = phoneNumber;
    }

    // Getter methods
    public String getOperator() {
        return operator;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    @Override
    public void apply(BankAccount account) {
        // Fatura ödemesi, bakiyeden düşülecek
        account.setBalance(account.getBalance() - getAmount());
    }

    @Override
    public String toString() {
        return "PhoneBillPaymentTransaction{" +
                "operator='" + operator + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", amount=" + getAmount() +
                ", date=" + getDate() +
                '}';
    }
}