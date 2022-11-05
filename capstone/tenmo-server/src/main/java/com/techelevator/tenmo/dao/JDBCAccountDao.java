package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class JdbcAccountDao implements AccountDao{

    //@Autowired
    private JdbcTemplate jdbcTemplate;
    public JdbcAccountDao(DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
    }
   // public JdbcAccountDao (JdbcTemplate jdbcTemplate) {
      // this.jdbcTemplate = jdbcTemplate;
   // }
    @Override
    public BigDecimal getBalance(int userId) {
        String sql = "SELECT balance FROM account WHERE user_id = ?;";
        SqlRowSet results = null;
        BigDecimal balance = null;
        try {
            results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                balance = results.getBigDecimal("balance");
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return balance;
    }

    @Override
    public BigDecimal getBalanceByAccountId(int accountId) {
        String sql = "SELECT balance FROM account WHERE account_id = ?;";
        SqlRowSet results;
        BigDecimal balance = null;
        try {
            results = jdbcTemplate.queryForRowSet(sql, accountId);
            if (results.next()) {
                balance = results.getBigDecimal("balance");
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return balance;
    }

    @Override
    public BigDecimal addToBalance(BigDecimal amountToAdd, int id) {
        Account account = findAccountByAccountId(id);
        BigDecimal updatedBalance = account.getBalance().add(amountToAdd);
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?;";
        try {
            jdbcTemplate.update(sql, updatedBalance, id);
        } catch (Exception e) {
            e.getMessage();
        }
        return account.getBalance();
    }

    @Override
    public BigDecimal subtractFromBalance(BigDecimal amountToSubtract, int id) {
        Account account = findAccountByAccountId(id);
        BigDecimal updatedBalance = account.getBalance().subtract(amountToSubtract);
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?;";
        try {
            jdbcTemplate.update(sql, updatedBalance, id);
        } catch (Exception e) {
            e.getMessage();
        }
        return account.getBalance();
    }

    @Override
    public Account findAccountByUserId(int userId) {
        String sql = "SELECT balance, user_id, account_id FROM account WHERE user_id = ?;";
        Account account = null;
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()){
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public Account findAccountByAccountId(int id) {
        Account account = null;
        String sql = "SELECT * FROM account WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()){
            account = mapRowToAccount(results);
        }
        return account;
    }
    private Account mapRowToAccount(SqlRowSet result) {
        Account account = new Account();
        account.setBalance(result.getBigDecimal("balance"));
        account.setUserId(result.getInt("user_id"));
        account.setAccountId(result.getInt("account_id"));
        return account;
    }
}
