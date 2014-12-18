package com.github.spikevlg.balanceofphone.model;

import com.google.common.base.MoreObjects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/**
 * Class represents request message to servlet and rules to xml transformation.
 */
@XmlRootElement(name="request")
@XmlAccessorType(XmlAccessType.FIELD)
public class PhoneServiceRequest {
    /**
     * Type of request to servlet.
     */
    @XmlElement(required = true, name="request-type")
    private RequestType type;
    /**
     * User login. Login represents phone number.
     */
    @XmlElement(required = true, name="login")
    private String login;
    /**
     * User password.
     */
    @XmlElement(required = true, name="password")
    private String password;

    public PhoneServiceRequest() {
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
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
        // don't show user password
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("type", type)
                .add("login", login)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, login, password);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PhoneServiceRequest){
            PhoneServiceRequest other = (PhoneServiceRequest)obj;
            return Objects.equals(type, other.type)
                    && Objects.equals(login, other.login)
                    && Objects.equals(password, other.password);
        } else return false;
    }
}
