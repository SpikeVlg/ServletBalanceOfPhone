package com.github.spikevlg.balanceofphone.utils;

import com.github.spikevlg.balanceofphone.BalanceOfPhoneBaseException;

/**
 * Exception throws if user password is incorrect.
 */
public class UserAuthenticationException extends BalanceOfPhoneBaseException {
    public UserAuthenticationException(String errorMessage){
        super(errorMessage);
    }
}
