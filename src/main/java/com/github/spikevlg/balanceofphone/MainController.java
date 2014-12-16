package com.github.spikevlg.balanceofphone;

import com.github.spikevlg.balanceofphone.model.*;
import com.github.spikevlg.balanceofphone.persist.PhoneServiceDAO;
import com.github.spikevlg.balanceofphone.persist.UserExistsException;
import com.github.spikevlg.balanceofphone.utils.DataValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Main logic of servlet.
 */
@RestController
public class MainController {
    private Logger logger = LoggerFactory.getLogger(MainController.class);;
    private PhoneServiceDAO phoneServiceDAO;
    private DataValidator dataValidator;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public MainController(@Qualifier("hibernate") PhoneServiceDAO phoneServiceDAO, DataValidator dataValidator
            , PasswordEncoder passwordEncoder){
        this.phoneServiceDAO = phoneServiceDAO;
        this.dataValidator = dataValidator;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * The main servlet method.
     * Get request and send response.
     * @param request object contains information about request.
     * @return response object
     */
    @RequestMapping(value = "/balance_of_phone", method = RequestMethod.POST)
    public PhoneServiceResponse processRequest(@RequestBody PhoneServiceRequest request) {
        logger.debug("get request %s", request);

        RequestType requestType = RequestType.getRequestTypeByCode(request.getType());

        if (requestType == null){
            logger.debug("request type is null");
            return new PhoneServiceResponse(ResponseCode.UNKNOWN_ACTION);
        }

        switch (requestType){
            case ADD_USER:
                ResponseCode responseCode = addNewUser(request.getLogin(), request.getPassword());
                logger.debug("Adding user. Returned %s code.", responseCode);
                return new PhoneServiceResponse(responseCode);
            case GET_BALANCE:
                return getUserBalance(request.getLogin(), request.getPassword());
            default:
                //by default we don't have others actions
                logger.debug("Unknown action %s", requestType);
                return new PhoneServiceResponse(ResponseCode.UNKNOWN_ACTION);
        }
    }

    /**
     * Add new user in data base.
     * @param dirtyUserLogin user login from request
     * @param password of user
     * @return result of user adding
     */
    ResponseCode addNewUser(String dirtyUserLogin, String password) {
        logger.debug("adding new user with login %s", dirtyUserLogin);

        if (!dataValidator.isValideLogin(dirtyUserLogin)) {
            logger.debug("invalid login %s", dirtyUserLogin);
            return ResponseCode.INVALID_LOGIN;
        }

        String errorMsg = dataValidator.validatePassword(password);
        if (errorMsg != null) {
            logger.debug("too weak password %s", password);
            return ResponseCode.WEAK_PASSWORD;
        }

        String cleanPhoneLogin = dataValidator.cleanLogin(dirtyUserLogin);
        // First we need to check existing user without synchronize blocking
        PhoneUser existsUser = phoneServiceDAO.findByLogin(cleanPhoneLogin);
        if (existsUser != null){
            logger.debug("user with login %s already exists", cleanPhoneLogin);
            return ResponseCode.USER_ALREADY_EXISTS;
        }

        PhoneUser newUser = new PhoneUser(null, cleanPhoneLogin, passwordEncoder.encode(password));

        try {
            phoneServiceDAO.insertPhoneUser(newUser);
            logger.debug("success add user with login %s", cleanPhoneLogin);
            return ResponseCode.OK;
        } catch (UserExistsException ex){
            logger.debug("user with login %s already exists. Created just now in other thread."
                    , cleanPhoneLogin);
            // here we check existing user with synchronize blocking
            return ResponseCode.USER_ALREADY_EXISTS;
        }
    }

    /**
     * Get user phone balance.
     * @param dirtyUserLogin user login from request
     * @param password of user from request
     * @return result of getting balance
     */
    PhoneServiceResponse getUserBalance(String dirtyUserLogin, String password){
        logger.debug("getting balance for user %s", dirtyUserLogin);

        if (!dataValidator.isValideLogin(dirtyUserLogin)) {
            logger.debug("invalid login %s", dirtyUserLogin);
            return new PhoneServiceResponse(ResponseCode.INVALID_LOGIN);
        }

        String cleanPhoneLogin = dataValidator.cleanLogin(dirtyUserLogin);
        PhoneUser phoneUser = phoneServiceDAO.findByLogin(cleanPhoneLogin);
        if (phoneUser == null){
            logger.debug("user not found");
            return new PhoneServiceResponse(ResponseCode.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(password, phoneUser.getHashPassword())){
            logger.debug("return authentication error code");
            return new PhoneServiceResponse(ResponseCode.AUTHENTICATION_ERROR);
        }

        double balance = phoneServiceDAO.getBalanceById(phoneUser.getId());
        PhoneServiceResponse response = new PhoneServiceResponse(ResponseCode.OK);
        response.setBalance(balance);
        logger.debug("return response %s", response);
        return response;
    }
}
