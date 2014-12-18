package com.github.spikevlg.balanceofphone.model;

import com.google.common.base.MoreObjects;

import javax.xml.bind.annotation.*;
import java.util.Objects;

/**
 * Class represent servlet response.
 */
@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.FIELD)
public class PhoneServiceResponse {
    /**
     * Code of response.
     */
    @XmlElement(required = true, name="result-code")
    private ResponseCode code;

    /**
     * Balance of user. This field not required and need for balance type request.
     */
    @XmlElement(required = false, name="bal")
    private Double balance;

    public PhoneServiceResponse() {
    }

    public PhoneServiceResponse(ResponseCode code){
        this.code = code;
    }

    public PhoneServiceResponse(ResponseCode code, Double balance){
        this.code = code;
        this.balance = balance;
    }

    public ResponseCode getCode() {
        return code;
    }

    public void setCode(ResponseCode code) {
        this.code = code;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("code", code)
                .add("balance", balance)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, balance);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PhoneServiceResponse){
            PhoneServiceResponse other = (PhoneServiceResponse)obj;
            return Objects.equals(code, other.code)
                    && Objects.equals(balance, other.balance);
        } else return false;
    }
}
