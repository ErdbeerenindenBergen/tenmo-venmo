package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
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
    public Transfer getTransferByTransferId(int transferId) {
        return transferDao.getTransferById(transferId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public List<Transfer> getAllTransfersByUserId(int userId) {
        return transferDao.getAllTransfersByUserId(userId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "/pending/{userId}", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfers(int userId) {
        return transferDao.getPendingRequests(userId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "/send", method = RequestMethod.POST)
    public String sendTransfer(int userFromId, int userToId, BigDecimal amount) {
        return transferDao.sendTransfer(userFromId, userToId, amount);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "/request", method = RequestMethod.GET)
    public String requestTransfer(int userFrom, int userTo, BigDecimal amount) {
        return transferDao.requestTransfer(userFrom, userTo, amount);
    }

    ///BEGINNING
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public String create(@RequestBody Transfer transfer) throws TransferException {

        Account fromAccount = accountDao.findAccountByAccountId(transfer.getAccountFrom());
        Account toAccount = accountDao.findAccountByAccountId(transfer.getAccountTo());
        BigDecimal amount = transfer.getAmount();

        //if-statements below verify that accounts exist and there are sufficient funds to make transfer
        if (fromAccount == null || toAccount == null) {
            throw new TransferException("These accounts could not be retrieved.");
        } else if (amount.compareTo(fromAccount.getBalance()) > 0) {
            throw new TransferException("You do not have enough funds in your account to make this transfer.");
        }

        // Transfer is created below.
        //wondering whether we need to set transfer status to 1 or if that's automatic...maybe that should go in transfer class?
        String transferSuccessReport = transferDao.sendTransfer(fromAccount.getAccountId(), toAccount.getAccountId(), amount);
        // Before sending transfer, the status of the transfer must be checked.
        // For status 1 (pending), do not proceed, 3 (rejected) should only possible in updating method
        // We are looking for status 2 (approved).
        // Transfer is created and logged.
        if (transfer.getTransferStatusId() == 2) {
            accountDao.subtractFromBalance(amount, transfer.getAccountFrom());
            accountDao.addToBalance(amount, transfer.getAccountTo());
        }
        return transferSuccessReport;
    }

    @RequestMapping(path = "/update", method = RequestMethod.PUT)
    public String updateTransferRequest(@PathVariable int transferId, @RequestBody Transfer transfer) throws TransferException {
        String transferSuccessReport;

        Account accountFrom = accountDao.findAccountByAccountId(transfer.getAccountFrom());
        Account accountTo = accountDao.findAccountByAccountId(transfer.getAccountTo());
        BigDecimal amount = transfer.getAmount();

        if (accountFrom == null || accountTo == null) {
            throw new TransferException("These accounts could not be retrieved.");
        } else if (amount.compareTo(accountFrom.getBalance()) > 0) {
            throw new TransferException("You do not have enough funds in your account to make this transfer.");
        }

        // If the transferStatusId already 2 (approved), update the transfer but do not update account balances.
        if (transferDao.getTransferById(transferId).getTransferStatusId() == 2) {
            return transferDao.updateTransferRequest(transfer, transferId);
        }
        transferSuccessReport = transferDao.updateTransferRequest(transfer, transferId);

        // If transferStatusId = 2 (Approved), sendTransfer
        if (transferDao.getTransferById(transferId).getTransferStatusId() == 2) {
            accountDao.subtractFromBalance(amount, transfer.getAccountFrom());
            accountDao.addToBalance(amount, transfer.getAccountTo());
        }
        return transferSuccessReport;
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

//    @RequestMapping(path = "my-transfers", method = RequestMethod.GET)
//    public List<Transfer> viewAllTransfersByUserId(Principal user) {
//        String username = user.getName();
//        int userId = userDao.findIdByUsername(username);
//        return transferDao.getAllTransfersByUserId(userId);
//    }

}
