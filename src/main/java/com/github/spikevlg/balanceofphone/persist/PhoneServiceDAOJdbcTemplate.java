package com.github.spikevlg.balanceofphone.persist;

import com.github.spikevlg.balanceofphone.model.PhoneUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class PhoneServiceDAOJdbcTemplate implements PhoneServiceDAO{
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert userInserter;

    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_HASH_PASSWORD = "hash_password";

    @Autowired
    public PhoneServiceDAOJdbcTemplate(JdbcTemplate jdbcTemplate, SimpleJdbcInsert userInserter){
        this.jdbcTemplate = jdbcTemplate;
        this.userInserter = userInserter;
    }

    static class PhoneUserMapper implements RowMapper<PhoneUser> {
        @Override
        public PhoneUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            PhoneUser phoneUser = new PhoneUser();
            phoneUser.setLogin(rs.getString(COLUMN_LOGIN));
            phoneUser.setHashPassword(rs.getString(COLUMN_HASH_PASSWORD));
            return phoneUser;
        }
    }

    @Override
    public PhoneUser findByLogin(String login) {
        final String SELECT_USER_BY_ID = "SELECT id, login, hash_password FROM users WHERE login = ?";
        try {
            PhoneUser phoneUser = jdbcTemplate.queryForObject(SELECT_USER_BY_ID
                    , new PhoneUserMapper(), login);
            return phoneUser;
        } catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public PhoneUser findById(Integer userID) {
        final String SELECT_USER_BY_ID = "SELECT id, login, hash_password FROM users WHERE id = ?";
        try {
            PhoneUser phoneUser = jdbcTemplate.queryForObject(SELECT_USER_BY_ID
                    , new PhoneUserMapper(), userID);
            return phoneUser;
        } catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public synchronized void insertPhoneUser(PhoneUser newPhoneUser) throws UserExistsException{
        if (newPhoneUser == null){
            throw new IllegalArgumentException("newPhoneUser must be not null");
        }

        // we need to check existing user in synchronize block.
        PhoneUser oldPhoneUser = findByLogin(newPhoneUser.getLogin());
        if (oldPhoneUser != null){
            throw new UserExistsException("User with login '" + newPhoneUser.getLogin()
                    + "' already exists!");
        }

        Map<String, Object> newUserProperty = new HashMap<>();
        newUserProperty.put(COLUMN_LOGIN, newPhoneUser.getLogin());
        newUserProperty.put(COLUMN_HASH_PASSWORD, newPhoneUser.getHashPassword());

        Number insertedUserId = userInserter.executeAndReturnKey(newUserProperty);

        newPhoneUser.setId(insertedUserId.intValue());

        final String INSERT_BALANCE = "insert into phone_balance(user_id, balance) values (?, ?)";
        int balanceId = jdbcTemplate.update(INSERT_BALANCE, insertedUserId.intValue(), 0.0);
    }

    @Override
    public double getBalanceByLogin(String login) {
        return jdbcTemplate.queryForObject(
                "select b.balance from phone_balance b"
                        + " inner join users u"
                        + " on u.id = b.user_id"
                        + " where u.login = ?"
                , Double.class, login);
    }

    @Override
    public double getBalanceById(Integer userId) {
        return jdbcTemplate.queryForObject("select balance from phone_balance where user_id = ?"
                , Double.class, userId);
    }
}
