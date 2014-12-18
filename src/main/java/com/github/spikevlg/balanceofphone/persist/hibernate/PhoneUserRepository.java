package com.github.spikevlg.balanceofphone.persist.hibernate;
import com.github.spikevlg.balanceofphone.model.PhoneUser;
import org.springframework.data.repository.Repository;

/**
 * Interface for work get and save phone user in database.
 */
public interface PhoneUserRepository extends Repository<PhoneUser, Integer> {
    /**
     * Find phoneUser by user ID.
     * @param userId - user ID in database
     * @return phoneUser object or null if not exists user
     */
    PhoneUser findById(Integer userId);

    /**
     * Find phoneUser by login.
     * @param login - user login
     * @return phoneUser object or null if not exists user
     */
    PhoneUser findByLogin(String login);

    /**
     * Save phone user in database.
     * @param user
     * @return
     */
    PhoneUser save(PhoneUser user);
}
