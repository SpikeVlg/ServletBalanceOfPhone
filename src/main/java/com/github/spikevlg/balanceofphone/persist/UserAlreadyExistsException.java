package com.github.spikevlg.balanceofphone.persist;


import com.github.spikevlg.balanceofphone.BalanceOfPhoneBaseException;

/**
 * Exception thorws when user already exists in database.
 */
public class UserAlreadyExistsException extends BalanceOfPhoneBaseException {
    public UserAlreadyExistsException(String message){
        super(message);
    }
}
