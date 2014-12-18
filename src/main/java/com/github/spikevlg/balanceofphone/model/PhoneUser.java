package com.github.spikevlg.balanceofphone.model;

import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.util.Objects;

/**
 * Class represents phone user.
 */
@Entity
@Table(name = "users", uniqueConstraints = {  @UniqueConstraint(columnNames = "login") })
public class PhoneUser {
    /**
     * User ID in database.
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * User login. Login represents phone number without special characters.
     */
    @Column(name = "login", unique = true, nullable = false, length = 255)
    private String login;

    /**
     * Hash of user password.
     */
    @Column(name="hash_password", nullable = false, length = 255)
    private String hashPassword;

    public PhoneUser(Integer id, String login, String hashPassword) {
        this.id = id;
        this.login = login;
        this.hashPassword = hashPassword;
    }

    public PhoneUser(String login, String hashPassword) {
        this.login = login;
        this.hashPassword = hashPassword;
    }

    public PhoneUser(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String password) {
        this.hashPassword = password;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("id", id)
                .add("login", login)
                .add("hashPassword", hashPassword)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, hashPassword);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PhoneUser){
            PhoneUser other = (PhoneUser)obj;
            return Objects.equals(id, other.id)
                    && Objects.equals(login, other.login)
                    && Objects.equals(hashPassword, other.hashPassword);
        } else return false;
    }
}
