package com.techelevator.tenmo.dao;
import java.math.BigDecimal;
import com.techelevator.tenmo.model.Account;

public interface AccountDao {

    //Need to get balance, user ID for each account, and add/subtract balance

    BigDecimal getBalance(int userId);

    BigDecimal getBalanceByAccountId(int accountId);

    BigDecimal addToBalance(BigDecimal amountToAdd, int id);

    BigDecimal subtractFromBalance(BigDecimal amountToSubtract, int id);

    Account findAccountByUserId(int userId);

    public Account findAccountByAccountId(int id);
}
