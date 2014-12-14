package com.github.spikevlg.balanceofphone.persist;

import com.github.spikevlg.balanceofphone.model.PhoneUser;

/**
 * DAO interface for phone service.
 */
public interface PhoneServiceDAO {
    /**
     * Find phoneUser by login.
     * @param login - user login
     * @return phoneUser object or null if not exists user
     */
    PhoneUser findByLogin(String login);

    /**
     * Find phoneUser by user ID.
     * @param userID - user ID in database
     * @return phoneUser object or null if not exists user
     */
    PhoneUser findById(Integer userID);

    /**
     * Create new phone user and set balance 0.00 value.
     * @param phoneUser object for insert into database
     * @throws UserExistsException if user already exists
     */
    void insertPhoneUser(PhoneUser phoneUser) throws UserExistsException;

    /**
     * Get user balance by login.
     * @param login of user
     * @return phone balance
     */
    double getBalanceByLogin(String login);

    /**
     * Get user balance by user ID.
     * @param userId of user
     * @return phone balance
     */
    double getBalanceById(Integer userId);
}
