package com.github.spikevlg.balanceofphone.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlEnum(Integer.class)

public enum ResponseCode {
    @XmlEnumValue("0") OK
    , @XmlEnumValue("1") USER_ALREADY_EXISTS
    , @XmlEnumValue("2") BAD_LOGIN
    , @XmlEnumValue("3") BAD_PASSWORD
    , @XmlEnumValue("4") UNKNOWN_ACTION
    , @XmlEnumValue("5") UNKNOWN_ERROR
    , @XmlEnumValue("6") AUTHENTIFICATION_ERROR
    , @XmlEnumValue("7") USER_NOT_FOUND;
}
