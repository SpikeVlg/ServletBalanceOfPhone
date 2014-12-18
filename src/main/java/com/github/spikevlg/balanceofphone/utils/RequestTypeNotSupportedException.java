package com.github.spikevlg.balanceofphone.utils;


import com.github.spikevlg.balanceofphone.BalanceOfPhoneBaseException;

/**
 * Exception for enum RequestType if request type is not supported.
 */
public class RequestTypeNotSupportedException extends BalanceOfPhoneBaseException {
    public RequestTypeNotSupportedException(String errorMessage){
        super(errorMessage);
    }
}
