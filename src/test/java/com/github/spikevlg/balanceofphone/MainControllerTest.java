package com.github.spikevlg.balanceofphone;


import com.github.spikevlg.balanceofphone.model.PhoneUser;
import com.github.spikevlg.balanceofphone.persist.PhoneServiceDAO;
import com.github.spikevlg.balanceofphone.persist.UserAlreadyExistsException;
import com.github.spikevlg.balanceofphone.utils.DataValidator;
import com.github.spikevlg.balanceofphone.utils.UserAuthenticationException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MainControllerTest {
    private MainController mainController;
    private PhoneServiceDAO phoneServiceDAO;
    private PasswordEncoder passwordEncoder;
    private DataValidator dataValidator;

    private String DIRTY_PHONE_LOGIN = "+7 955 444 3326";
    private String CLEAN_PHONE_LOGIN = "79554443326";
    private String PASSWORD = "password";
    private String HASH_PASSWORD = "hashPassword";
    private double BALANCE = 10.0;
    private double DOUBLE_DELTA = 0.00001;


    /**
     * Creates mock objects.
     */
    @Before
    public void setUp() {
        dataValidator = mock(DataValidator.class);
        phoneServiceDAO = mock(PhoneServiceDAO.class);
        passwordEncoder = mock(PasswordEncoder.class);

        mainController = new MainController(phoneServiceDAO, dataValidator, passwordEncoder);
    }

    @Test(expected = UserAuthenticationException.class)
    public void getUserBalanceFail() throws Exception{
        PhoneUser phoneUser = new PhoneUser(CLEAN_PHONE_LOGIN, HASH_PASSWORD);

        when(dataValidator.getPurePhoneNumber(CLEAN_PHONE_LOGIN)).thenReturn(CLEAN_PHONE_LOGIN);
        when(phoneServiceDAO.findByLogin(CLEAN_PHONE_LOGIN)).thenReturn(phoneUser);
        when(passwordEncoder.matches(PASSWORD, HASH_PASSWORD)).thenReturn(false);

        mainController.getUserBalance(CLEAN_PHONE_LOGIN, PASSWORD);
    }

    @Test
    public void getUserBalanceSuccess() throws Exception{
        PhoneUser phoneUser = new PhoneUser(CLEAN_PHONE_LOGIN, HASH_PASSWORD);

        when(dataValidator.getPurePhoneNumber(DIRTY_PHONE_LOGIN)).thenReturn(CLEAN_PHONE_LOGIN);
        when(phoneServiceDAO.findByLogin(CLEAN_PHONE_LOGIN)).thenReturn(phoneUser);
        when(passwordEncoder.matches(PASSWORD, HASH_PASSWORD)).thenReturn(true);
        when(phoneServiceDAO.getBalanceById(phoneUser.getId())).thenReturn(BALANCE);

        assertEquals(BALANCE, mainController.getUserBalance(DIRTY_PHONE_LOGIN, PASSWORD), DOUBLE_DELTA);
    }

    @Test
    public void addNewUserSuccess() throws Exception{
        when(dataValidator.getPurePhoneNumber(DIRTY_PHONE_LOGIN)).thenReturn(CLEAN_PHONE_LOGIN);
        when(phoneServiceDAO.findByLogin(CLEAN_PHONE_LOGIN)).thenReturn(null);

        mainController.addNewUser(DIRTY_PHONE_LOGIN, PASSWORD);

        verify(phoneServiceDAO, times(1)).insertPhoneUser(any());
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void addNewUserFail() throws Exception{
        when(dataValidator.getPurePhoneNumber(DIRTY_PHONE_LOGIN)).thenReturn(CLEAN_PHONE_LOGIN);
        when(phoneServiceDAO.findByLogin(CLEAN_PHONE_LOGIN)).thenReturn(new PhoneUser());

        mainController.addNewUser(DIRTY_PHONE_LOGIN, PASSWORD);
    }
}
