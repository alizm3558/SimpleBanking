package com.eteration.simplebanking.model;

public class PhoneBillPaymentTransaction extends Transaction {

    private String operator;
    private String phoneNumber;

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