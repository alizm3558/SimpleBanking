package com.eteration.simplebanking.controller;


import lombok.Data;

// This class is a place holder you can change the complete implementation
@Data
public class TransactionStatus {

    private String status;       // İşlem durumu (OK veya ERROR)
    private String approvalCode;  // İşleme özgü bir onay kodu (UUID)

    // Boş yapıcı metot (JPA veya framework'ler için gerekebilir)
    public TransactionStatus() {}

    // Parametreli yapıcı metot
    public TransactionStatus(String status, String approvalCode) {
        this.status = status;
        this.approvalCode = approvalCode;
    }


}
