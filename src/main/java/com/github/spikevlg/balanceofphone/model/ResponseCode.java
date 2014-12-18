package com.github.spikevlg.balanceofphone.model;

import com.github.spikevlg.balanceofphone.BalanceOfPhoneBaseException;
import com.github.spikevlg.balanceofphone.persist.UserAlreadyExistsException;
import com.github.spikevlg.balanceofphone.utils.*;
import com.google.common.collect.ImmutableMap;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import java.util.Map;

/**
 * Enum of response code.
 * Servlet sends response with this codes.
 */
@XmlType
@XmlEnum(Integer.class)
public enum ResponseCode {
    @XmlEnumValue("0") OK
    , @XmlEnumValue("1") USER_ALREADY_EXISTS
    , @XmlEnumValue("2") INVALID_LOGIN
    , @XmlEnumValue("3") WEAK_PASSWORD
    , @XmlEnumValue("4") UNKNOWN_ACTION
    , @XmlEnumValue("5") UNKNOWN_ERROR
    , @XmlEnumValue("6") AUTHENTICATION_ERROR
    , @XmlEnumValue("7") USER_NOT_FOUND;

    /**
     * Mapping application exceptions to response codes.
     */
    private static Map<Class<? extends Exception>, ResponseCode> exceptionToCodes;

    /**
     * Init map for representation exceptions and codes.
     */
    static {
        ImmutableMap.Builder<Class<? extends Exception>, ResponseCode> builder = ImmutableMap.builder();
        exceptionToCodes = builder.put(UserAlreadyExistsException.class, USER_ALREADY_EXISTS)
                .put(InvalidLoginException.class, INVALID_LOGIN)
                .put(WeakPasswordException.class, WEAK_PASSWORD)
                .put(RequestTypeNotSupportedException.class, UNKNOWN_ACTION)
                .put(UserAuthenticationException.class, AUTHENTICATION_ERROR)
                .put(UserNotFoundException.class, USER_NOT_FOUND)
                .put(BalanceOfPhoneBaseException.class, UNKNOWN_ERROR)
                .build();
    }

    /**
     * Get response code by exception.
     * @param ex catched exception
     * @return response code
     */
    public static ResponseCode getCodeByException(Class<? extends Exception> ex){
        if (exceptionToCodes.containsKey(ex)){
            return exceptionToCodes.get(ex);
        } else {
            return ResponseCode.UNKNOWN_ERROR;
        }
    }
}
