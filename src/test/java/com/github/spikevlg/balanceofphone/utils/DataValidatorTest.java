package com.github.spikevlg.balanceofphone.utils;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class DataValidatorTest {
    private DataValidator validator = new DataValidator();
    private DataValidator spyValidator;

    /**
     * Creates mock objects.
     */
    @Before
    public void setUp() {
        spyValidator = spy(new DataValidator());
    }

    @Test
    public void isValidPassword(){
        assertTrue(0 < validator.isValidPassword("1").length());
        assertTrue(0 < validator.isValidPassword("11111111111").length());
        assertTrue(0 < validator.isValidPassword("1bbcc").length());
        assertTrue(0 == validator.isValidPassword("1bC1bC1bC1bC!!!!").length());
        assertTrue(0 == validator.isValidPassword("11111bbbbCCCC!!!!").length());
    }

    @Test
    public void validatePasswordSuccess() throws WeakPasswordException{
        doReturn("").when(spyValidator).isValidPassword(anyString());
        spyValidator.validatePassword("11111bbbbCCCC!!!!");
    }

    @Test(expected = WeakPasswordException.class)
    public void validatePaswordFail() throws WeakPasswordException{
        doReturn("NOT_EMPTY").when(spyValidator).isValidPassword(anyString());
        spyValidator.validatePassword("1");
    }

    @Test
    public void isValidPhoneNumber(){
        assertTrue(validator.isValidPhoneNumber("79995551111"));
        assertTrue(validator.isValidPhoneNumber("+79995551111"));
        assertTrue(validator.isValidPhoneNumber("+7999-555-11-11"));
        assertFalse(validator.isValidPhoneNumber("aaabbb"));
        assertFalse(validator.isValidPhoneNumber(""));
        assertFalse(validator.isValidPhoneNumber("7+79995551111"));
        assertFalse(validator.isValidPhoneNumber("a+79995551111"));
        assertFalse(validator.isValidPhoneNumber("79995551111s"));
    }

    @Test
    public void getPurePhoneNumberSuccess() throws InvalidLoginException{
        assertEquals("79995551111", validator.getPurePhoneNumber("79995551111"));
        assertEquals("79995551111", validator.getPurePhoneNumber("+79995551111"));
        assertEquals("79995551111", validator.getPurePhoneNumber("7999-555-11-11"));
        assertEquals("79995551111", validator.getPurePhoneNumber("7999 555 11 11"));
        assertEquals("79995551111", validator.getPurePhoneNumber("+7999-555 11 11"));
    }

    @Test(expected = InvalidLoginException.class)
    public void getPurePhoneNumberFail() throws InvalidLoginException {
        doReturn(false).when(spyValidator).isValidPhoneNumber(anyString());
        spyValidator.getPurePhoneNumber("test");
    }
}
