package com.github.spikevlg.balanceofphone.model;

public class PhoneUser {

    private Integer id;
    private String login;
    private String hashPassword;

    public PhoneUser(Integer id, String login, String hashPassword) {
        this.id = id;
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

}
