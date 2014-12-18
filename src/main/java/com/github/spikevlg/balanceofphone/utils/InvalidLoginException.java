package com.github.spikevlg.balanceofphone.utils;

import com.github.spikevlg.balanceofphone.BalanceOfPhoneBaseException;

/**
 * Invalid login of user
 */
public class InvalidLoginException extends BalanceOfPhoneBaseException{
    public InvalidLoginException(String errorMessage){
        super(errorMessage);
    }
}
