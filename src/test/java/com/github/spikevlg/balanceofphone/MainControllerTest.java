package com.github.spikevlg.balanceofphone;


import com.github.spikevlg.balanceofphone.model.PhoneServiceResponse;
import com.github.spikevlg.balanceofphone.model.PhoneUser;
import com.github.spikevlg.balanceofphone.model.ResponseCode;
import com.github.spikevlg.balanceofphone.persist.PhoneServiceDAO;
import com.github.spikevlg.balanceofphone.utils.DataValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MainControllerTest {
    private MainController mainController;
    private PhoneServiceDAO phoneServiceDAO;
    private PasswordEncoder passwordEncoder;

    /**
     * Creates mock objects.
     */
    @Before
    public void setUp() {
        DataValidator dataValidator = new DataValidator();
        phoneServiceDAO = mock(PhoneServiceDAO.class);
        passwordEncoder = mock(PasswordEncoder.class);
        mainController = new MainController(phoneServiceDAO, dataValidator, passwordEncoder);
    }

    @Test
    public void getUserBalanceInvalidLogin() {
        PhoneServiceResponse invalidLogin = new PhoneServiceResponse(ResponseCode.INVALID_LOGIN);
        assertEquals(invalidLogin, mainController.getUserBalance(null, null));
        assertEquals(invalidLogin, mainController.getUserBalance("", null));
    }

    @Test
    public void getUserBalanceSuccess() {
        String CLEAN_PHONE_LOGIN = "79554443326";
        String PASSWORD = "password";
        String HASH_PASSWORD = "hashPassword";
        double BALANCE = 10.0;

        PhoneServiceResponse responseSuccess = new PhoneServiceResponse(ResponseCode.OK);
        responseSuccess.setBalance(BALANCE);
        PhoneUser phoneUser = new PhoneUser(1, CLEAN_PHONE_LOGIN, HASH_PASSWORD);

        when(phoneServiceDAO.findByLogin(CLEAN_PHONE_LOGIN)).thenReturn(phoneUser);
        when(passwordEncoder.matches(PASSWORD, HASH_PASSWORD)).thenReturn(true);
        when(phoneServiceDAO.getBalanceById(phoneUser.getId())).thenReturn(BALANCE);

        assertEquals(responseSuccess, mainController.getUserBalance(CLEAN_PHONE_LOGIN, PASSWORD));
    }
}
