package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    //make sure base API_URL includes "transfer"

    TransferDao transferDao;

    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping (path = "transfer/{transactionId}", method = RequestMethod.GET)
    public Transfer getTransferByTransactionId(int transactionId) {
        return transferDao.getTransferById(transactionId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "transfer/user/{userId}", method = RequestMethod.GET)
    public List<Transfer> getAllTransfersByUserId(int userId) {
// Should change method below in TransferDao to getTransfersByUserId(userId)
        return transferDao.getAllTransfersByUserId(userId);
    }

    //FOR BELOW: I think the transferDao method below should use Transfers transfer instead of BigDecimal amount

//    @PreAuthorize("hasRole('ROLE_USER')")
//    @ResponseStatus(HttpStatus.CREATED)
//    @RequestMapping(path = "/send/{fromId}/{toId}", method = RequestMethod.POST)
//    public boolean sendMoney(@PathVariable int fromId, @PathVariable int toId, @Valid @RequestBody Transfers transfer) {
//        return transferDao.sendTransfer(fromId, toId, transfer);
//    }


    //FOR BELOW: NEED transferDao.getAllTransfers() METHOD IN DAO

//    @RequestMapping(path="s", method = RequestMethod.GET)
//    public List<Transfers> getAllTransfers() {
//        return transferDao.getAllTransfers();
//    }

    //FOR BELOW: NEED transferDao.updateTransfer(transferId, transfer) METHOD IN DAO

//    @PreAuthorize("hasRole('ROLE_USER')")
//    @RequestMapping(path = "/{transactionId}/update", method = RequestMethod.PUT)
//    public boolean updateTransfer(@PathVariable int transactionId, @RequestBody Transfers transfer) {
//        return transferDao.updateTransfer(transactionId, transfer);
//    }

    //FOR BELOW: NEED transferDao.getTransferStatus(transferId) METHOD IN DAO

//    @PreAuthorize("hasRole('ROLE_USER')")
//    @RequestMapping(path = "/status/{transactionId}", method = RequestMethod.GET)
//    public String getTransferStatusName(@PathVariable int transactionId) {
//        return transferDao.getTransferStatus(transactionId);
//    }



//---------------------------------------------------------------------------------------------------------------------

    //probably not a helpful method below but saving for later just in case


//    @RequestMapping (path = "/{fromId}/{toId}/{amount}", method = RequestMethod.GET)
//    public String sendTransfer(int userFromId, int userToId, BigDecimal amount);
//        return transferDao.getTransferById();


}
