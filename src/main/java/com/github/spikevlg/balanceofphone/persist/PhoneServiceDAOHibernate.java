package com.github.spikevlg.balanceofphone.persist;


import com.github.spikevlg.balanceofphone.model.PhoneUser;
import com.github.spikevlg.balanceofphone.model.UserBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Transactional
@Component("hibernate")
public class PhoneServiceDAOHibernate implements PhoneServiceDAO{

    private PhoneUserRepository phoneUserRepository;

    private UserBalanceRepository userBalanceRepository;

    @Autowired
    public PhoneServiceDAOHibernate(PhoneUserRepository phoneUserRepository
            , UserBalanceRepository userBalanceRepository) {
        this.phoneUserRepository = phoneUserRepository;
        this.userBalanceRepository = userBalanceRepository;
    }

    @Override
    public PhoneUser findByLogin(String login) {
        return phoneUserRepository.findByLogin(login);
    }

    @Override
    public PhoneUser findById(Integer userID) {
        return phoneUserRepository.findById(userID);
    }

    @Override
    public synchronized void insertPhoneUser(PhoneUser phoneUser) throws UserExistsException {
        phoneUserRepository.save(phoneUser);

        UserBalance userBalance = new UserBalance(null, phoneUser, 0.0);
        userBalanceRepository.save(userBalance);
    }

    @Override
    public double getBalanceByLogin(String login) {
        PhoneUser user = phoneUserRepository.findByLogin(login);
        UserBalance userBalance = userBalanceRepository.findByUser(user);
        return userBalance.getBalance();
    }

    @Override
    public double getBalanceById(Integer userId) {
        PhoneUser user = phoneUserRepository.findById(userId);
        UserBalance userBalance = userBalanceRepository.findByUser(user);
        return userBalance.getBalance();
    }
}
