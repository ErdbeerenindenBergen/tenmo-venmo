package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {

    public List <Transfer> getAllTransfersByUserId(int userId);

    List<Transfer> seeAllTransfers();

    public Transfer getTransferById(int transferId);
    public String sendTransfer(int userFromId, int userToId, BigDecimal amount);
    public String requestTransfer(int userFrom, int userTo, BigDecimal amount);
    public List<Transfer> getPendingRequests(int userId);
    public String updateTransferRequest(Transfer transfer, int statusId);
}
