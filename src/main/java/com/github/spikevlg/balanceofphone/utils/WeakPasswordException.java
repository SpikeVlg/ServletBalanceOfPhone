package com.github.spikevlg.balanceofphone.utils;

import com.github.spikevlg.balanceofphone.BalanceOfPhoneBaseException;

/**
 * Invalid password of user
 */
public class WeakPasswordException extends BalanceOfPhoneBaseException {
    public WeakPasswordException(String errorMessage){
        super(errorMessage);
    }
}
