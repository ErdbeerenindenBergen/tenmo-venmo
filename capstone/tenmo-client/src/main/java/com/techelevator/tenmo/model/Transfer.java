package com.techelevator.tenmo.model;
import java.math.BigDecimal;
import java.text.NumberFormat;

public class Transfer {

    private int transferId;
    private int transferTypeId;
    private int transferStatusId;
    private int accountFrom;
    private int accountTo;
    private BigDecimal amount;
    private String transferType;
    private String transferStatus;
    private String userFrom;
    private String userTo;

    public Transfer(){

    }

        public int getTransferId() {
            return transferId;
        }

        public void setTransferId(int transferId) {
            this.transferId = transferId;
        }

        public int getTransferTypeId() {
            return transferTypeId;
        }

        public void setTransferTypeId(int transferTypeId) {
            this.transferTypeId = transferTypeId;
        }

        public int getTransferStatusId() {
            return transferStatusId;
        }

        public void setTransferStatusId(int transferStatusId) {
            this.transferStatusId = transferStatusId;
        }

        public int getAccountFrom() {
            return accountFrom;
        }

        public void setAccountFrom(int accountFrom) {
            this.accountFrom = accountFrom;
        }

        public int getAccountTo() {
            return accountTo;
        }

        public void setAccountTo(int accountTo) {
            this.accountTo = accountTo;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getTransferType() {
            return transferType;
        }

        public void setTransferType(String transferType) {
            this.transferType = transferType;
        }

        public String getTransferStatus() {
            return transferStatus;
        }

        public void setTransferStatus(String transferStatus) {
            this.transferStatus = transferStatus;
        }

        public String getUserFrom() {
            return userFrom;
        }

        public void setUserFrom(String userFrom) {
            this.userFrom = userFrom;
        }

        public String getUserTo() {
            return userTo;
        }

        public void setUserTo(String userTo) {
            this.userTo = userTo;
        }

    public String displayTransferType(Integer transferTypeId) {
        if (transferTypeId == 1) {
            return "Request Money";
        } else if (transferTypeId == 2) {
            return "Send Money";
        }
        return transferTypeId.toString();
    }

    public String displayTransferStatus(Integer transferStatusId) {
        if (transferStatusId == 1) {
            return "Pending";
        } else if (transferStatusId == 2) {
            return "Approved";
        } else if (transferStatusId == 3) {
            return "Rejected";
        } else {
            return transferStatusId.toString();
        }
    }

    public String displayAsCurrency(BigDecimal bigDecimal) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(bigDecimal);
    }

    public String transferDetailsPrintOut() {

        return  "\n-------------------------------------------------------------" +
                "\n Transfer Details: " +
                "\n-------------------------------------------------------------" +
                "\n Transfer ID:    " + transferId +
                "\n From Account:   " + accountFrom +
                "\n To Account:     " + accountTo +
                "\n Type:           " + displayTransferType(transferTypeId) +
                "\n Status:         " + displayTransferStatus(transferStatusId) +
                "\n Amount:         " + displayAsCurrency(amount) +
                "\n-------------------------------------------------------------";
    }

}
