package com.github.spikevlg.balanceofphone;

import com.github.spikevlg.balanceofphone.model.*;
import com.github.spikevlg.balanceofphone.persist.PhoneServiceDAO;
import com.github.spikevlg.balanceofphone.persist.UserExistsException;
import com.github.spikevlg.balanceofphone.utils.DataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
public class MainController {
    @Autowired
    PhoneServiceDAO dao;

    @Autowired
    DataSource dataSource;

    @Autowired
    DataValidator validator;

    @Autowired
    PasswordEncoder passwordEncoder;


    /**
     * The main servlet method.
     * Get request and send response.
     * @param request object contains information about request.
     * @return response object
     */
    @RequestMapping(value = "/balance_of_phone", method = RequestMethod.POST)
    public PhoneServiceResponse processRequest(@RequestBody PhoneServiceRequest request) {
        RequestType requestType = RequestType.getRequestTypeByCode(request.getType());

        if (requestType == null){
            return new PhoneServiceResponse(ResponseCode.UNKNOWN_ACTION);
        }

        switch (requestType){
            case ADD_USER:
                ResponseCode responseCode = addNewUser(request.getLogin(), request.getPassword());
                return new PhoneServiceResponse(responseCode);
            case GET_BALANCE:
                return getUserBalance(request.getLogin(), request.getPassword());
            default:
                //by default we don't have others actions
                return new PhoneServiceResponse(ResponseCode.UNKNOWN_ERROR);
        }
    }

    ResponseCode addNewUser(String dirtyPhoneLogin, String password) {
        if (!validator.isValideLogin(dirtyPhoneLogin)) {
            return ResponseCode.BAD_LOGIN;
        }

        String errorMsg = validator.validatePassword(password);
        if (errorMsg != null) {
            return ResponseCode.BAD_PASSWORD;
        }

        String cleanPhoneLogin = validator.cleanLogin(dirtyPhoneLogin);
        // First we need to check existing user without synchronize blocking
        PhoneUser existsUser = dao.findByLogin(cleanPhoneLogin);
        if (existsUser != null){
            return ResponseCode.USER_ALREADY_EXISTS;
        }

        PhoneUser newUser = new PhoneUser(null, cleanPhoneLogin, passwordEncoder.encode(password));

        try {
            dao.insertPhoneUser(newUser);
            return ResponseCode.OK;
        } catch (UserExistsException ex){
            // here we check existing user with synchronize blocking
            return ResponseCode.USER_ALREADY_EXISTS;
        }
    }

    PhoneServiceResponse getUserBalance(String dirtyPhoneLogin, String password){
        String cleanPhoneLogin = validator.cleanLogin(dirtyPhoneLogin);
        PhoneUser phoneUser = dao.findByLogin(cleanPhoneLogin);
        if (phoneUser == null){
            return new PhoneServiceResponse(ResponseCode.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(password, phoneUser.getHashPassword())){
            return new PhoneServiceResponse(ResponseCode.AUTHENTIFICATION_ERROR);
        }

        double balance = dao.getBalanceById(phoneUser.getId());
        PhoneServiceResponse response = new PhoneServiceResponse(ResponseCode.OK);
        response.setBalance(balance);
        return response;
    }
}
