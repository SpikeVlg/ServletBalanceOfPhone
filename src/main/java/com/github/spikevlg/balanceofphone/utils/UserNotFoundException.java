package com.github.spikevlg.balanceofphone.utils;

import com.github.spikevlg.balanceofphone.BalanceOfPhoneBaseException;

/**
 * Throws if user not found.
 */
public class UserNotFoundException extends BalanceOfPhoneBaseException{
    public UserNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
