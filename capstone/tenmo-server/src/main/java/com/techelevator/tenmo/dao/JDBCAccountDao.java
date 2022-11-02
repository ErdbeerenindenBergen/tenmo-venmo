package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public class JDBCAccountDao implements AccountDao{
    @Override
    public BigDecimal getBalance(int userId) {
        return null;
    }

    @Override
    public BigDecimal addToBalance(BigDecimal amountToAdd, int id) {
        return null;
    }

    @Override
    public BigDecimal subtractFromBalance(BigDecimal amountToSubtract, int id) {
        return null;
    }

    @Override
    public Account findUserById(int userId) {
        return null;
    }

    @Override
    public Account findAccountById(int id) {
        return null;
    }
}
