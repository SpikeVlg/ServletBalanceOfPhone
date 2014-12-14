package com.github.spikevlg.balanceofphone.persist;


public class UserExistsException extends Exception {
    public UserExistsException(String message){
        super(message);
    }
}
