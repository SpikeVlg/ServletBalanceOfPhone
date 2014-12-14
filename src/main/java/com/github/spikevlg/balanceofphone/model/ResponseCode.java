package com.github.spikevlg.balanceofphone.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Enum of response code.
 * Servlet sends response with this codes.
 */
@XmlType
@XmlEnum(Integer.class)
public enum ResponseCode {
    @XmlEnumValue("0") OK
    , @XmlEnumValue("1") USER_ALREADY_EXISTS
    , @XmlEnumValue("2")INVALID_LOGIN
    , @XmlEnumValue("3")WEAK_PASSWORD
    , @XmlEnumValue("4") UNKNOWN_ACTION
    , @XmlEnumValue("5") UNKNOWN_ERROR
    , @XmlEnumValue("6")AUTHENTICATION_ERROR
    , @XmlEnumValue("7") USER_NOT_FOUND;
}
