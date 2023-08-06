package com.KeyzHire.Rewards;

import java.time.LocalDate;

class Transaction {
    private String customerId;
    private double amount;
    private LocalDate transactionDate;

    public Transaction(String customerId, double amount, LocalDate transactionDate) {
        this.customerId = customerId;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }
}