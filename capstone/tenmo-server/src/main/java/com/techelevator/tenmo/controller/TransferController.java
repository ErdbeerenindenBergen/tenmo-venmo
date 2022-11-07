package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
//"transfer" is added to BASE_API_URL
@RequestMapping("transfer")
public class TransferController {

    TransferDao transferDao;
    AccountDao accountDao;

    public TransferController(TransferDao transferDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping (path = "/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferByTransferId(@PathVariable int transferId) {
        Transfer transfer = transferDao.getTransferById(transferId);
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transfer could be found.");
        } else {
            return transfer;
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping (path = "/all", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers() {
        List<Transfer> transfers = transferDao.seeAllTransfers();

        if (transfers == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transfers could be found.");
        } else {
            return transfers;
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public List<Transfer> allTransfersByUserId(@PathVariable int userId) {
        List<Transfer> allTransfersByUserId = transferDao.getAllTransfersByUserId(userId);
        if (allTransfersByUserId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transfers were found.");
        } else {
            return allTransfersByUserId;
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "/user/{userId}/pending", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfers(@PathVariable int userId) {
        List<Transfer> pendingTransfers = transferDao.getPendingRequests(userId);
        if (pendingTransfers == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transfers were found.");
        } else {
            return pendingTransfers;
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/send", method = RequestMethod.POST)
    public String sendTransfer(@Valid @RequestBody Transfer transfer) {
        return transferDao.sendTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "/request", method = RequestMethod.POST)
    public String requestTransfer(@Valid @RequestBody Transfer transfer) {
        return transferDao.requestTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "/update/{statusId}", method = RequestMethod.PUT)
    public String updateTransfer(@Valid @RequestBody Transfer transfer, @PathVariable int statusId) {
        return transferDao.updateTransferRequest(transfer, statusId);
    }



}
