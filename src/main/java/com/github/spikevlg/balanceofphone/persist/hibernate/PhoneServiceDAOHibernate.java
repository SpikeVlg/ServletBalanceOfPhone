package com.github.spikevlg.balanceofphone.persist.hibernate;


import com.github.spikevlg.balanceofphone.model.PhoneUser;
import com.github.spikevlg.balanceofphone.model.UserBalance;
import com.github.spikevlg.balanceofphone.persist.PhoneServiceDAO;
import com.github.spikevlg.balanceofphone.persist.UserAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Objects;

@Component("hibernate")
public class PhoneServiceDAOHibernate implements PhoneServiceDAO {
    private static final Logger logger = LoggerFactory.getLogger(PhoneServiceDAOHibernate.class);;

    private PhoneUserRepository phoneUserRepository;

    private UserBalanceRepository userBalanceRepository;

    @Autowired
    public PhoneServiceDAOHibernate(PhoneUserRepository phoneUserRepository
            , UserBalanceRepository userBalanceRepository) {
        this.phoneUserRepository = phoneUserRepository;
        this.userBalanceRepository = userBalanceRepository;
    }

    /**
     * Find phoneUser by login.
     * @param login - user login
     * @return phoneUser object or null if not exists user
     */
    @Override
    public PhoneUser findByLogin(String login) {
        logger.debug("Find by login '{}'", login);
        Objects.requireNonNull(login, "login cannot be null");
        PhoneUser phoneUser = phoneUserRepository.findByLogin(login);
        logger.debug("Return {}", phoneUser);
        return phoneUser;
    }

    /**
     * Find phoneUser by user ID.
     * @param userId - user ID in database
     * @return phoneUser object or null if not exists user
     */
    @Override
    public PhoneUser findById(Integer userId) {
        logger.debug("Find by user ID '{}'", userId);
        Objects.requireNonNull(userId, "userId cannot be null");
        PhoneUser phoneUser = phoneUserRepository.findById(userId);
        logger.debug("Return {}", phoneUser);
        return phoneUser;
    }

    /**
     * Create new phone user and set balance 0.00 value.
     * @param phoneUser object for insert into database
     * @throws com.github.spikevlg.balanceofphone.persist.UserAlreadyExistsException if user already exists
     */
    @Transactional
    @Override
    public synchronized void insertPhoneUser(PhoneUser phoneUser) throws UserAlreadyExistsException {
        logger.debug("Try insert user {}", phoneUser);
        Objects.requireNonNull(phoneUser, "newPhoneUser cannot be null");

        phoneUserRepository.save(phoneUser);
        logger.debug("User added with id '{}'", phoneUser.getId());

        UserBalance userBalance = new UserBalance(null, phoneUser, 0.0);
        userBalanceRepository.save(userBalance);
        logger.debug("Balance added with id '{}'", userBalance.getId());
    }

    /**
     * Get user balance by login.
     * @param login of user
     * @return phone balance
     */
    @Override
    public double getBalanceByLogin(String login) {
        logger.debug("Get balance by login '{}'", login);
        Objects.requireNonNull(login, "newPhoneUser cannot be null");

        PhoneUser user = phoneUserRepository.findByLogin(login);
        UserBalance userBalance = userBalanceRepository.findByUser(user);

        logger.debug("Return balance {}", userBalance.getBalance());
        return userBalance.getBalance();
    }

    /**
     * Get user balance by user ID.
     * @param userId of user
     * @return phone balance
     */
    @Override
    public double getBalanceById(Integer userId) {
        logger.debug("Get balance by user ID {}", userId);
        Objects.requireNonNull(userId, "userID cannot be null");

        PhoneUser user = phoneUserRepository.findById(userId);
        UserBalance userBalance = userBalanceRepository.findByUser(user);

        logger.debug("Return balance {}", userBalance.getBalance());
        return userBalance.getBalance();
    }
}
