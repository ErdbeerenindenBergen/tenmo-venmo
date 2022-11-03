package com.techelevator.tenmo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TransferException extends Exception {
    public TransferException() {
        super();
    }

    public TransferException(String error) {
        super(error);
    }
}
