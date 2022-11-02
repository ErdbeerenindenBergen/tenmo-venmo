package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;
import com.techelevator.tenmo.model.Transfers;

public interface TransferDao {

    public List <Transfers> getAllTransfers(int userId);
    public Transfers getTransferById(int transactionId);
    public String sendTransfer(int userFromId, int userToId, BigDecimal amount);
}
