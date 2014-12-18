package com.github.spikevlg.balanceofphone.persist.hibernate;


import com.github.spikevlg.balanceofphone.model.PhoneUser;
import com.github.spikevlg.balanceofphone.model.UserBalance;
import org.springframework.data.repository.Repository;

/**
 * Interface for work get and save balance of user in database.
 */
public interface UserBalanceRepository extends Repository<UserBalance, Integer> {
    /**
     * Find balance by user.
     * @param user of phone services
     * @return balance of user structure
     */
    UserBalance findByUser(PhoneUser user);

    /**
     * Save info about balance in database.
     * @param userBalance structure of user balance
     * @return updated sturcture balance of user
     */
    UserBalance save(UserBalance userBalance);
}
