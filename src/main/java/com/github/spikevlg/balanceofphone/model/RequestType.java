package com.github.spikevlg.balanceofphone.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Types of request for servlet balance of phone.
 */
@XmlType
@XmlEnum(String.class)
public enum RequestType {
    /**
     * Add new user.
     */
    @XmlEnumValue("new-agt") ADD_USER
    /**
     * Get balance of user
     */
    , @XmlEnumValue("agt-bal") GET_BALANCE;
}
