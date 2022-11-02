package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfers;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

public class TransferController {

    //make sure base API_URL includes "/transfer"

    TransferDao transferDao;

    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public List<Transfers> getAllTransfers(int userId) {
        return transferDao.getAllTransfers(userId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping (path = "/{transactionId}", method = RequestMethod.GET)
//To follow naming convention, we should rename Transfers as Transfer -- singular
    public Transfers getTransferById(int transactionId) {
        return transferDao.getTransferById(transactionId);
    }

    //FOR BELOW: NEED transferDao.sendMoney(fromId, toId, transfer) METHOD IN DAO

//    @PreAuthorize("hasRole('ROLE_USER')")
//    @ResponseStatus(HttpStatus.CREATED)
//    @RequestMapping(path = "/request/{fromId}/{toId}", method = RequestMethod.POST)
//    public boolean requestMoney(@PathVariable int fromId, @PathVariable int toId, @Valid @RequestBody Transfers transfer) {
//        return transferDao.sendMoney(fromId, toId, transfer);
//    }

    //FOR BELOW: NEED transferDao.updateTransfer(transferId, transfer) METHOD IN DAO

//    @PreAuthorize("hasRole('ROLE_USER')")
//    @RequestMapping(path = "/{transferId}/update", method = RequestMethod.PUT)
//    public boolean updateTransfer(@PathVariable int transferId, @RequestBody Transfers transfer) {
//        return transferDao.updateTransfer(transferId, transfer);
//    }

    //FOR BELOW: NEED transferDao.getTransferStatus(transferId) METHOD IN DAO

//    @PreAuthorize("hasRole('ROLE_USER')")
//    @RequestMapping(path = "/status/{transferId}", method = RequestMethod.GET)
//    public String getTransferStatusName(@PathVariable int transferId) {
//        return transferDao.getTransferStatus(transferId);
//    }

    //---------------------------------------------------------------------------------------

    /*probably not a helpful method below but saving for later just in case

    */

//    @RequestMapping (path = "/{fromId}/{toId}/{amount}", method = RequestMethod.GET)
//    public String sendTransfer(int userFromId, int userToId, BigDecimal amount);
//        return transferDao.getTransferById();


}
