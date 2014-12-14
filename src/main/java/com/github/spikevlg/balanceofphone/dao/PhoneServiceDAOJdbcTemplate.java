package com.github.spikevlg.balanceofphone.dao;

import com.github.spikevlg.balanceofphone.model.PhoneUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PhoneServiceDAOJdbcTemplate implements PhoneServiceDAO{
    JdbcTemplate jdbcTemplate;

    @Autowired
    public PhoneServiceDAOJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    static class PhoneUserMapper implements RowMapper<PhoneUser> {
        @Override
        public PhoneUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            PhoneUser phoneUser = new PhoneUser();
            phoneUser.setUser(rs.getString("login"));
            phoneUser.setPassword(rs.getString("password"));
            return phoneUser;
        }
    }

    @Override
    public PhoneUser findByLogin(String login) {
        final String SELECT_USER_BY_ID = "SELECT id, login, password FROM users WHERE login = ?";
        PhoneUser phoneUser = jdbcTemplate.queryForObject(SELECT_USER_BY_ID
                , new PhoneUserMapper(), login);
        return phoneUser;
    }

    @Override
    public PhoneUser findById(Integer userID) {
        final String SELECT_USER_BY_ID = "SELECT id, login, password FROM users WHERE id = ?";
        PhoneUser phoneUser = jdbcTemplate.queryForObject(SELECT_USER_BY_ID
                , new PhoneUserMapper(), userID);
        return phoneUser;
    }

    @Override
    public synchronized void insertPhoneUser(PhoneUser newPhoneUser) {
        if (newPhoneUser == null){
            throw new IllegalArgumentException("newPhoneUser must be not null");
        }

        PhoneUser oldPhoneUser = findByLogin(newPhoneUser.getUser());
        if (oldPhoneUser == null){

        }

        final String INSERT_USER = "insert into users(login, password) values (?, ?)";
        jdbcTemplate.update(INSERT_USER, newPhoneUser.getUser(), newPhoneUser.getPassword());
    }
}
