package com.techelevator.tenmo.dao;
import java.math.BigDecimal;
import com.techelevator.tenmo.model.Account;

public interface AccountDao {
    BigDecimal getBalance(int userId);
    BigDecimal addToBalance(BigDecimal amountToAdd, int id);
    BigDecimal subtractFromBalance(BigDecimal amountToSubtract, int id);
    Account findUserById(int userId);
    public Account findAccountById(int id);
}
