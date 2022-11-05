package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.techelevator.tenmo.model.NoTransferFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import com.techelevator.tenmo.model.Transfer;

@Component
public class JdbcTransferDao implements TransferDao {
    AccountDao accountDao;
    private JdbcTemplate jdbcTemplate;
    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?);";
        jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    @Override
    public List<Transfer> getAllTransfersByUserId(int userId) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT transfer.*, tenmo_user.username FROM transfer " +
                     "JOIN account ON transfer.account_from = account.account_id OR transfer.account_to = account.account_id " +
                     "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                     "WHERE account.user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transferList.add(transfer);
        }
        return transferList;
    }

    @Override
    public Transfer getTransferById(int transferId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        } else {
            throw new NoTransferFound();
        }
        return transfer;
    }

//    @Override
//    public String sendTransfer(int userFromId, int userToId, BigDecimal amount) {
//        String successMessage;
//        if (userFromId == userToId) {
//            successMessage = "You can't send yourself money!";
//        } else if (amount.compareTo(accountDao.getBalance(userFromId)) == -1 && amount.compareTo(new BigDecimal(0)) == 1) {
//            String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
//                         "VALUES (2,2,?,?,?)";
//            jdbcTemplate.update(sql, userFromId, userToId);
//            accountDao.addToBalance(amount, userToId);
//            accountDao.subtractFromBalance(amount, userFromId);
//            successMessage = "Transfer processed successfully!";
//        } else {
//            successMessage = "There was an error during transfer because of a lack of funds or invalid user choice.";
//        } return successMessage;
//    }

    @Override
    public String sendTransfer(int userFromId, int userToId, BigDecimal amount) {
        if (userFromId == userToId) {
            return "You can't send yourself money!";
        }
        if (amount.compareTo(accountDao.getBalance(userFromId)) <= 0) {
            return "There was a problem processing your transfer.";
        } else {
            String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                         "VALUES (2,2,?,?,?);";
            jdbcTemplate.update(sql, userFromId, userToId);
            accountDao.addToBalance(amount, userToId);
            accountDao.subtractFromBalance(amount, userFromId);
            return "Transfer processed successfully!";
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
            return "There was a problem processing your request.";
        }
    }
        @Override
        public List<Transfer> getPendingRequests ( int userId){
            List<Transfer> pendingRequests = new ArrayList<>();
            String sql = "SELECT transfer.* FROM transfer " +
                    "JOIN account ON transfer.account_from = account.account_id OR transfer.account_to = account.account_id " +
                    "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                    "WHERE transfer_status_id = 1 AND (account_from = ? OR account_to = ?);";
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
            return "Update successful!";
        }
        //If they do not have a negative balance, update the request
        if (!(accountDao.getBalance(transfer.getAccountFrom()).compareTo(transfer.getAmount()) == -1)) {
            String sql = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?;";
            jdbcTemplate.update(sql, statusId, transfer.getTransferId());
            accountDao.addToBalance(transfer.getAmount(), transfer.getAccountTo());
            accountDao.subtractFromBalance(transfer.getAmount(), transfer.getAccountFrom());
            return "Update successful!";
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
            transfer.setUserFrom(results.getString("userTo"));
            transfer.setUserTo(results.getString("userFrom"));
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
