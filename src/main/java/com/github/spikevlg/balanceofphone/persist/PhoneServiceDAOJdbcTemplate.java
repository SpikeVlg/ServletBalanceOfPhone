package com.github.spikevlg.balanceofphone.persist;

import com.github.spikevlg.balanceofphone.model.PhoneUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation PhoneServiceDAO via Spring JdbcTemplate mechanism.
 */
@Component
public class PhoneServiceDAOJdbcTemplate implements PhoneServiceDAO{
    private Logger logger = LoggerFactory.getLogger(PhoneServiceDAOJdbcTemplate.class);;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert userInserter;
    private RowMapper<PhoneUser> phoneUserMapper;

    public static final String COLUMN_LOGIN = "login";
    public static final String COLUMN_HASH_PASSWORD = "hash_password";

    @Autowired
    public PhoneServiceDAOJdbcTemplate(JdbcTemplate jdbcTemplate
            , SimpleJdbcInsert userInserter, RowMapper<PhoneUser> phoneUserMapper
    ){
        this.jdbcTemplate = jdbcTemplate;
        this.userInserter = userInserter;
        this.phoneUserMapper = phoneUserMapper;
    }

    /**
     * Find phoneUser by login.
     * @param login - user login
     * @return phoneUser object or null if not exists user
     */
    @Override
    public PhoneUser findByLogin(String login) {
        logger.debug("find by login %s", login);
        final String SELECT_USER_BY_ID = "SELECT id, login, hash_password FROM users WHERE login = ?";
        try {
            PhoneUser phoneUser = jdbcTemplate.queryForObject(SELECT_USER_BY_ID
                    , phoneUserMapper, login);
            logger.debug("return %s", phoneUser);
            return phoneUser;
        } catch (EmptyResultDataAccessException ex){
            logger.debug("user not found");
            return null;
        }
    }

    /**
     * Find phoneUser by user ID.
     * @param userID - user ID in database
     * @return phoneUser object or null if not exists user
     */
    @Override
    public PhoneUser findById(Integer userID) {
        logger.debug("find by user ID %s", userID);
        final String SELECT_USER_BY_ID = "SELECT id, login, hash_password FROM users WHERE id = ?";
        try {
            PhoneUser phoneUser = jdbcTemplate.queryForObject(SELECT_USER_BY_ID
                    , phoneUserMapper, userID);
            logger.debug("return %s", phoneUser);
            return phoneUser;
        } catch (EmptyResultDataAccessException ex){
            logger.debug("user not found");
            return null;
        }
    }

    /**
     * Create new phone user and set balance 0.00 value.
     * @param newPhoneUser object for insert into database
     * @throws UserExistsException if user already exists
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public synchronized void insertPhoneUser(PhoneUser newPhoneUser) throws UserExistsException{
        logger.debug("try insert user %s", newPhoneUser);
        if (newPhoneUser == null){
            logger.error("newPhoneUser must be not null");
            throw new IllegalArgumentException("newPhoneUser must be not null");
        }

        // we need to check existing user in synchronize block.
        PhoneUser oldPhoneUser = findByLogin(newPhoneUser.getLogin());
        if (oldPhoneUser != null){
            logger.error("user with login %s already exists", newPhoneUser.getLogin());
            throw new UserExistsException("User with login '" + newPhoneUser.getLogin()
                    + "' already exists!");
        }

        Map<String, Object> newUserProperty = new HashMap<>();
        newUserProperty.put(COLUMN_LOGIN, newPhoneUser.getLogin());
        newUserProperty.put(COLUMN_HASH_PASSWORD, newPhoneUser.getHashPassword());

        Number insertedUserId = userInserter.executeAndReturnKey(newUserProperty);
        logger.debug("user added with id = %s", insertedUserId);

        newPhoneUser.setId(insertedUserId.intValue());

        final String INSERT_BALANCE = "insert into phone_balance(user_id, balance) values (?, ?)";
        int balanceId = jdbcTemplate.update(INSERT_BALANCE, insertedUserId.intValue(), 0.0);
        logger.debug("balance added with id = %s", balanceId);
    }

    /**
     * Get user balance by login.
     * @param login of user
     * @return phone balance
     */
    @Override
    public double getBalanceByLogin(String login) {
        logger.debug("get balance by login %s", login);
        double balance = jdbcTemplate.queryForObject(
                "select b.balance from phone_balance b"
                        + " inner join users u"
                        + " on u.id = b.user_id"
                        + " where u.login = ?"
                , Double.class, login);
        logger.debug("return balance %s", balance);
        return balance;
    }

    /**
     * Get user balance by user ID.
     * @param userId of user
     * @return phone balance
     */
    @Override
    public double getBalanceById(Integer userId) {
        logger.debug("get balance by user ID %s", userId);
        double balance = jdbcTemplate.queryForObject("select balance from phone_balance where user_id = ?"
                , Double.class, userId);
        logger.debug("return balance %s", balance);
        return balance;
    }
}
