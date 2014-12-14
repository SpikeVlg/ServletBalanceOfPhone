package com.github.spikevlg.balanceofphone.model;

/**
 * Type of request for servlet balance of phone.
 */
public enum RequestType {
    ADD_USER("new-agt")
    , GET_BALANCE("agt-bal");

    /**
     * XML code of request.
     */
    String code;

    RequestType(String code){
        this.code = code;
    }

    /**
     * Get RequestType by code. If requestType for this code not exists then return null.
     * @param code - code of request.
     * @return RequestType or null if not find RequestType.
     */
    public static RequestType getRequestTypeByCode(String code){
        for(RequestType currentRequestType: values()){
            if (code.equals(currentRequestType.code)){
                return currentRequestType;
            }
        }
        return null;
    }
}
