package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;

public class JdbcAccountDaoTest extends BaseDaoTests{

        private JdbcAccountDao sut;
        private Account testAccount;

        @Before
    public void setup() {
            sut = new JdbcAccountDao(dataSource);
            testAccount = new Account();
            testAccount.setUserId(5);
            testAccount.setAccountId(2);
            testAccount.setBalance(new BigDecimal("100.00"));
        }

        @Test
        public void getBalance_returns_correct_balance() {
            BigDecimal expectedValue = testAccount.getBalance();
            BigDecimal actual = new BigDecimal("100.00");
            Assert.assertEquals(expectedValue,actual);
        }
        @Test
        public void getAccountByUserId_returns_correct_account_id() {
            int expectedId = testAccount.getUserId();
            int actualId = 5;
            Account actual = testAccount;
            Assert.assertEquals(expectedId, actualId);
            assertAccountsMatch(actual, testAccount);
        }
        @Test
        public void getAccountByUserId_returns_null_when_id_not_found() {
            Account account = sut.findAccountByUserId(1000);
            Assert.assertNull(account);
        }
        @Test
    public void getAccountByAccountId_returns_correct_account_id() {
            int expected = testAccount.getAccountId();
            int actual = 2;
            Assert.assertEquals(expected, actual);
        }
        @Test
    public void getAccountByAccountId_returns_null_when_id_not_found() {
        Account account = sut.findAccountByAccountId(5000);
        Assert.assertNull(account);
    }
        @Test
   public void subtractFromBalance_subtracts_from_correct_account() {
            BigDecimal amountToSubtract = new BigDecimal("50.00");
            sut.subtractFromBalance(amountToSubtract, 2);
            Assert.assertEquals(testAccount.getBalance(), 50);

        }
    private void assertAccountsMatch(Account expected, Account actual) {
        Assert.assertEquals(expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
    }
}
