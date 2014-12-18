package com.github.spikevlg.balanceofphone;

import com.github.spikevlg.balanceofphone.model.*;
import com.github.spikevlg.balanceofphone.persist.PhoneServiceDAO;
import com.github.spikevlg.balanceofphone.persist.UserAlreadyExistsException;
import com.github.spikevlg.balanceofphone.utils.*;
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
    private final static Logger logger = LoggerFactory.getLogger(MainController.class);;
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
        logger.debug("Get request: {}", request);
        try {
            if (request == null
                    || request.getType() == null
                    || request.getLogin() == null
                    || request.getPassword() == null){
                throw new RequestTypeNotSupportedException("Request is not supported. Request - " + request);
            }

            switch (request.getType()){
                case ADD_USER:
                    addNewUser(request.getLogin(), request.getPassword());
                    return new PhoneServiceResponse(ResponseCode.OK);
                case GET_BALANCE:
                    double balance = getUserBalance(request.getLogin(), request.getPassword());
                    return new PhoneServiceResponse(ResponseCode.OK, balance);
                default:
                    //by default we don't have others actions
                    throw new RequestTypeNotSupportedException("Action '" + request.getType() + "' not supported");
            }
        }
        catch (BalanceOfPhoneBaseException ex){
            ResponseCode errorResponseCode = ResponseCode.getCodeByException(ex.getClass());
            logger.error("Request {}, errorCode - {}, error - {}", request, errorResponseCode, ex.getMessage());
            return new PhoneServiceResponse(errorResponseCode);
        }
    }

    /**
     * Add new user in data base.
     * @param dirtyPhoneNumber user login from request
     * @param password of user
     * @throws UserAlreadyExistsException if user already exists
     * @throws InvalidLoginException if user login is incorrect
     * @throws WeakPasswordException if user password is incorrect
     */
    void addNewUser(String dirtyPhoneNumber, String password) throws UserAlreadyExistsException
            , InvalidLoginException, WeakPasswordException
    {
        logger.debug("Adding new user with login '{}'", dirtyPhoneNumber);
        String purePhoneNumber = dataValidator.getPurePhoneNumber(dirtyPhoneNumber);
        dataValidator.validatePassword(password);

        // First we need to check existing user without synchronize blocking
        PhoneUser existsUser = phoneServiceDAO.findByLogin(purePhoneNumber);
        if (existsUser != null) {
            throw new UserAlreadyExistsException("Cannot add user '" + purePhoneNumber + "'. User already exists");
        }

        PhoneUser newUser = new PhoneUser(purePhoneNumber, passwordEncoder.encode(password));
        phoneServiceDAO.insertPhoneUser(newUser);
        logger.info("Success add user '{}'", purePhoneNumber);
    }

    /**
     * Get user phone balance.
     * @param dirtyPhoneNumber user login from request
     * @param password of user from request
     * @return user balance
     * @throws UserAuthenticationException if user authentication failed
     * @throws UserNotFoundException if user not found
     * @throws InvalidLoginException if user login incorrect
     * @throws WeakPasswordException if user password incorrect
     */
    double getUserBalance(String dirtyPhoneNumber, String password) throws InvalidLoginException
            , WeakPasswordException, UserAuthenticationException, UserNotFoundException
    {
        logger.debug("Getting balance for user '{}'", dirtyPhoneNumber);
        String purePhoneNumber = dataValidator.getPurePhoneNumber(dirtyPhoneNumber);
        dataValidator.validatePassword(password);

        PhoneUser phoneUser = phoneServiceDAO.findByLogin(purePhoneNumber);
        if (phoneUser == null){
            throw new UserNotFoundException("User '" + purePhoneNumber + "' not found");
        }
        // check password with hash from database
        if (!passwordEncoder.matches(password, phoneUser.getHashPassword())){
            throw new UserAuthenticationException("Password of user '" + purePhoneNumber + "' is incorrect");
        }

        double balance = phoneServiceDAO.getBalanceById(phoneUser.getId());
        logger.debug("For user '{}' return balance {}", purePhoneNumber, balance);
        return balance;
    }
}
