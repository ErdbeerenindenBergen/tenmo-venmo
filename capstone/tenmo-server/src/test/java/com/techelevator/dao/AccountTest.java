package com.techelevator.dao;

import com.techelevator.tenmo.model.Account;
import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;


import org.junit.Assert;
import org.junit.jupiter.api.Test;
import com.techelevator.tenmo.model.*;

public class AccountTest {
    Account account = new Account();

    @Test
    public void setAccountId_returns_correct_id() {
        account.setAccountId(3);
        Assert.assertEquals(account.getAccountId(), 3);
    }
    @Test
    public void setUserId_returns_correct_id() {
        account.setUserId(4);
        Assert.assertEquals(account.getUserId(), 4);
    }
    @Test
    public void setAccountBalance_returns_correct_balance() {
        account.setBalance(BigDecimal.valueOf(500));
        Assert.assertEquals(account.getBalance(), BigDecimal.valueOf(500));
    }
}
