package com.github.spikevlg.balanceofphone.contentprovider;


import com.github.spikevlg.balanceofphone.model.PhoneUser;
import com.github.spikevlg.balanceofphone.persist.PhoneServiceDAOJdbcTemplate;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
//@ImportResource("classpath:database_config.xml")
public class ContentProvider {
    /**
     * Create passwordEncoder object
     * @return passwordEncoder object
     */
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Create dataSource object with disable autocommit mode.
     * Url located in file "application.properties".
     * @param connectionUrl connection url
     * @return create dataSource object
     */
    @Bean(destroyMethod="close")
    public DataSource getDataSource(@Value("${spring.datasource.url}") String connectionUrl){
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(connectionUrl);
        basicDataSource.setDefaultAutoCommit(false);
        return basicDataSource;
    }


    @Bean
    @Autowired
    public SimpleJdbcInsert getUserInserter(DataSource dataSource){
        return new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingColumns(PhoneServiceDAOJdbcTemplate.COLUMN_LOGIN
                        , PhoneServiceDAOJdbcTemplate.COLUMN_HASH_PASSWORD)
                .usingGeneratedKeyColumns("id");
    }

    @Bean
    public RowMapper<PhoneUser> getPhoneUserMapper(){
        return new RowMapper<PhoneUser>() {
            @Override
            public PhoneUser mapRow(ResultSet rs, int rowNum) throws SQLException {
                PhoneUser phoneUser = new PhoneUser();
                phoneUser.setLogin(rs.getString(PhoneServiceDAOJdbcTemplate.COLUMN_LOGIN));
                phoneUser.setHashPassword(rs.getString(PhoneServiceDAOJdbcTemplate.COLUMN_HASH_PASSWORD));
                return phoneUser;
            }
        };
    }

}
