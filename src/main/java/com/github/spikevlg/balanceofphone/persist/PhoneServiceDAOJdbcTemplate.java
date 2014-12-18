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
import java.util.Objects;

/**
 * Implementation PhoneServiceDAO via Spring JdbcTemplate mechanism.
 */
@Component("jdbctemplate")
public class PhoneServiceDAOJdbcTemplate implements PhoneServiceDAO{
    private static final Logger logger = LoggerFactory.getLogger(PhoneServiceDAOJdbcTemplate.class);;
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
        logger.debug("Find by login '{}'", login);
        Objects.requireNonNull(login, "login cannot be null");

        final String SELECT_USER_BY_ID = "SELECT id, login, hash_password FROM users WHERE login = ?";
        try {
            PhoneUser phoneUser = jdbcTemplate.queryForObject(SELECT_USER_BY_ID
                    , phoneUserMapper, login);
            logger.debug("Return {}", phoneUser);
            return phoneUser;
        } catch (EmptyResultDataAccessException ex){
            logger.error("User witt login '{}' not found", login);
            return null;
        }
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

        final String SELECT_USER_BY_ID = "SELECT id, login, hash_password FROM users WHERE id = ?";
        try {
            PhoneUser phoneUser = jdbcTemplate.queryForObject(SELECT_USER_BY_ID
                    , phoneUserMapper, userId);
            logger.debug("Return {}", phoneUser);
            return phoneUser;
        } catch (EmptyResultDataAccessException ex){
            logger.error("User with id '{}' not found", userId);
            return null;
        }
    }

    /**
     * Create new phone user and set balance 0.00 value.
     * @param newPhoneUser object for insert into database
     * @throws UserAlreadyExistsException if user already exists
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public synchronized void insertPhoneUser(PhoneUser newPhoneUser) throws UserAlreadyExistsException {
        logger.debug("Try insert user {}", newPhoneUser);
        Objects.requireNonNull(newPhoneUser, "newPhoneUser cannot be null");

        // we need to check existing user in synchronize block because other thread
        // can to create user
        PhoneUser oldPhoneUser = findByLogin(newPhoneUser.getLogin());
        if (oldPhoneUser != null){
            logger.error("User with login '{}' already exists", newPhoneUser.getLogin());
            throw new UserAlreadyExistsException("User with login '" + newPhoneUser.getLogin()
                    + "' already exists!");
        }

        Map<String, Object> newUserProperty = new HashMap<>();
        newUserProperty.put(COLUMN_LOGIN, newPhoneUser.getLogin());
        newUserProperty.put(COLUMN_HASH_PASSWORD, newPhoneUser.getHashPassword());

        Number insertedUserId = userInserter.executeAndReturnKey(newUserProperty);
        logger.debug("User added with id '{}'", insertedUserId);

        newPhoneUser.setId(insertedUserId.intValue());

        final String INSERT_BALANCE = "insert into phone_balance(user_id, balance) values (?, ?)";
        int balanceId = jdbcTemplate.update(INSERT_BALANCE, insertedUserId.intValue(), 0.0);
        logger.debug("Balance added with id '{}'", balanceId);
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

        double balance = jdbcTemplate.queryForObject(
                "select b.balance from phone_balance b"
                        + " inner join users u"
                        + " on u.id = b.user_id"
                        + " where u.login = ?"
                , Double.class, login);
        logger.debug("Return balance {}", balance);
        return balance;
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

        double balance = jdbcTemplate.queryForObject("select balance from phone_balance where user_id = ?"
                , Double.class, userId);
        logger.debug("Return balance {}", balance);
        return balance;
    }
}
