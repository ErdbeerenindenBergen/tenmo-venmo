package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;
import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {

    public List <Transfer> getAllTransfers(int userId);
    public Transfer getTransferById(int transactionId);
    public String sendTransfer(int userFromId, int userToId, BigDecimal amount);
}
