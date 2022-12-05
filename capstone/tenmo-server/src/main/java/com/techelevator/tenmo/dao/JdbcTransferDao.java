package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.techelevator.tenmo.model.NoTransferFound;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import com.techelevator.tenmo.model.Transfer;

@Component
public class JdbcTransferDao implements TransferDao {

    AccountDao accountDao;
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, AccountDao accountDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = accountDao;
    }

    public void createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?);";
        jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    @Override
    public List<Transfer> getAllTransfersByUserId(int userId) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT t.*, u.user_id AS userFrom, v.user_id AS userTo, u.username AS userFromUsername, v.username AS userToUsername FROM transfer t\n" +
                "JOIN account a ON t.account_from = a.account_id\n" +
                "JOIN account b ON t.account_to = b.account_id\n" +
                "JOIN tenmo_user u ON a.user_id = u.user_id\n" +
                "JOIN tenmo_user v ON b.user_id = v.user_id\n" +
                "WHERE a.user_id = ? OR b.user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transferList.add(transfer);
        }
        return transferList;
    }

    @Override
    public List<Transfer> seeAllTransfers() {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT t.*, u.user_id AS userFrom, v.user_id AS userTo, u.username AS userFromUsername, v.username AS userToUsername FROM transfer t\n" +
                "JOIN account a ON t.account_from = a.account_id\n" +
                "JOIN account b ON t.account_to = b.account_id\n" +
                "JOIN tenmo_user u ON a.user_id = u.user_id\n" +
                "JOIN tenmo_user v ON b.user_id = v.user_id\n";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transferList.add(transfer);
        }
        return transferList;
    }

    @Override
    public Transfer getTransferById(int transferId) {
        Transfer transfer;
        String sql = "SELECT t.*, u.user_id AS userFrom, v.user_id AS userTo, u.username AS userFromUsername, v.username AS userToUsername FROM transfer t\n" +
                "JOIN account a ON t.account_from = a.account_id\n" +
                "JOIN account b ON t.account_to = b.account_id\n" +
                "JOIN tenmo_user u ON a.user_id = u.user_id\n" +
                "JOIN tenmo_user v ON b.user_id = v.user_id\n" +
                "WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        } else {
            throw new NoTransferFound();
        }
        return transfer;
    }

    @Override
    public String sendTransfer(int userFromAccountId, int userToAccountId, BigDecimal amount) {
        if (userFromAccountId == userToAccountId) {
            return "You can't send yourself money!";
        } else if (amount.compareTo(accountDao.getBalanceByAccountId(userFromAccountId)) == -1 && amount.compareTo(new BigDecimal(0)) == 1) {
            String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                         "VALUES (2,2,?,?,?);";
            jdbcTemplate.update(sql, userFromAccountId, userToAccountId, amount);
            accountDao.addToBalance(amount, userToAccountId);
            accountDao.subtractFromBalance(amount, userFromAccountId);
            return "Transfer processed successfully!";
        } else {
            return "There was a problem processing your transfer. You may have entered zero or a negative number.";
        }
    }

    @Override
    public String requestTransfer(int userFromId, int userToId, BigDecimal amount) {
        if (userFromId == userToId) {
            return "You can't request money from yourself!";
        }
        if (amount.compareTo(new BigDecimal(0)) == 1) {
            String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (1,1,?,?,?);";
            jdbcTemplate.update(sql, userFromId, userToId, amount);
            return "Request has been sent!";
        } else {
            return "There was a problem processing your request. You may have entered zero or a negative number.";
        }
    }

    public List<Transfer> getPendingRequests (int userId){
        List<Transfer> pendingRequests = new ArrayList<>();
        String sql = "SELECT t.*, u.user_id AS userFrom, v.user_id AS userTo, u.username AS userFromUsername, v.username AS userToUsername FROM transfer t\n" +
                "JOIN account a ON t.account_from = a.account_id\n" +
                "JOIN account b ON t.account_to = b.account_id\n" +
                "JOIN tenmo_user u ON a.user_id = u.user_id\n" +
                "JOIN tenmo_user v ON b.user_id = v.user_id\n" +
                "WHERE (a.user_id = ? OR b.user_id = ?) AND transfer_status_id = 1;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            pendingRequests.add(transfer);
        }
        return pendingRequests;
    }

    @Override
    public String updateTransferRequest(Transfer transfer, int statusId) {
        if (statusId == 3) {
            String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;";
            jdbcTemplate.update(sql, statusId, transfer.getTransferId());
            return "Update successful! This transfer has been rejected.";
        } else
        if (!(accountDao.getBalanceByAccountId(transfer.getAccountFrom()).compareTo(transfer.getAmount()) < 0)) {
            String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;";
            jdbcTemplate.update(sql, statusId, transfer.getTransferId());
            accountDao.addToBalance(transfer.getAmount(), transfer.getAccountTo());
            accountDao.subtractFromBalance(transfer.getAmount(), transfer.getAccountFrom());
            return "Update successful! This transfer has been approved and the requested TEbucks have been sent!";
        } else {
            return "You don't have enough funds for this transfer.";
        }
    }

    private Transfer mapRowToTransfer (SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setTransferTypeId(results.getInt("transfer_type_id"));
        transfer.setTransferStatusId(results.getInt("transfer_status_id"));
        transfer.setAccountFrom(results.getInt("account_from"));
        transfer.setAccountTo(results.getInt("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        try {
            transfer.setUserFrom(results.getInt("userFrom"));
            transfer.setUserTo(results.getInt("userTo"));
            transfer.setUserFromUsername(results.getString("userFromUsername"));
            transfer.setUserToUsername(results.getString("userToUsername"));
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            transfer.setTransferType(results.getString("transfer_type_desc"));
            transfer.setTransferStatus(results.getString("transfer_status"));
        } catch (Exception e) {
            e.getMessage();
        }
        return transfer;
    }

}
