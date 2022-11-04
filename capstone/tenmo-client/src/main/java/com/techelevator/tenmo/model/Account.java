package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    private BigDecimal balance;
    private int userId;
    private int accountId;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setUserId(int userId) {
    }
}
