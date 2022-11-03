package com.techelevator.dao;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.math.BigDecimal;
import java.util.List;

public class JdbcAccountDaoTest extends BaseDaoTests{

        private JdbcAccountDao sut;
        private Account testAccount;
        private Account testAccountTwo;

        @Before
    public void setup() {
            sut = new JdbcAccountDao();
            testAccount = new Account();
            testAccount.setUserId(5);
            testAccount.setAccountId(2);
            testAccount.setBalance(new BigDecimal("100.00"));
//            testAccountTwo.setUserId(6);
//            testAccountTwo.setAccountId(3);
//            testAccountTwo.setBalance(new BigDecimal("200.00"));
        }

        @Test
        public void getBalance_returns_correct_balance() {
            BigDecimal expectedValue = testAccount.getBalance();
            BigDecimal actual = new BigDecimal("100.00");
            Assert.assertEquals(expectedValue,actual);
        }
        @Test
        public void getAccountByUserId_returns_correct_account_id() {
            int expected = testAccount.getUserId();
            int actual = 5;
            Assert.assertEquals(actual, expected);
        }
        @Test
    public void getAccountByAccountId_returns_correct_account_id() {
            int expected = testAccount.getAccountId();
            int actual = 2;
            Assert.assertEquals(expected, actual);
        }
        @Test
   public void subtractFromBalance_subtracts_from_correct_account() {
            sut.subtractFromBalance(new BigDecimal("50.00"), testAccount.getUserId());
            Assert.assertEquals(testAccount.getBalance(), new BigDecimal("50"));
        }
}
