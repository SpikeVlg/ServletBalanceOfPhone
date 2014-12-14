package com.github.spikevlg.balanceofphone.dao;

import com.github.spikevlg.balanceofphone.model.PhoneUser;

public interface PhoneServiceDAO {
    PhoneUser findByLogin(String login);
    PhoneUser findById(Integer userID);
    void insertPhoneUser(PhoneUser phoneUser);
}
