package com.github.spikevlg.balanceofphone.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.FIELD)
public class PhoneServiceResponse {
    @XmlElement(required = true, name="result-code")
    private ResponseCode code;

    @XmlElement(required = false, name="bal")
    private Double balance;

    public PhoneServiceResponse() {
    }

    public PhoneServiceResponse(ResponseCode code){
        this.code = code;
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
}
