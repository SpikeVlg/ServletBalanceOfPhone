package com.github.spikevlg.balanceofphone.model;

import com.google.common.base.MoreObjects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="request")
@XmlAccessorType(XmlAccessType.FIELD)
public class PhoneServiceRequest {
    @XmlElement(required = true, name="request-type")
    private String type;

    @XmlElement(required = true, name="login")
    private String login;
    @XmlElement(required = true, name="password")
    private String password;

    public PhoneServiceRequest() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("type", type)
                .add("login", login)
                .add("password", password)
                .toString();
    }
}
