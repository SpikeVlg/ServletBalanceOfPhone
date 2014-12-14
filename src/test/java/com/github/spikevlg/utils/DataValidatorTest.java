package com.github.spikevlg.utils;


import com.github.spikevlg.balanceofphone.utils.DataValidator;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataValidatorTest {
    private DataValidator validator = new DataValidator();

    @Test
    public void validatePassword(){
        assertNotNull(validator.validatePassword("1"));
        assertNotNull(validator.validatePassword("11111111111"));
        assertNotNull(validator.validatePassword("1bbcc"));
        assertNull(validator.validatePassword("1bC1bC1bC1bC!!!!"));
        assertNull(validator.validatePassword("11111bbbbCCCC!!!!"));
    }

    @Test
    public void validateLogin(){
        assertTrue(validator.isValideLogin("79995551111"));
        assertTrue(validator.isValideLogin("+79995551111"));
        assertTrue(validator.isValideLogin("+7999-555-11-11"));
        assertFalse(validator.isValideLogin("aaabbb"));
        assertFalse(validator.isValideLogin(""));
        assertFalse(validator.isValideLogin("7+79995551111"));
        assertFalse(validator.isValideLogin("a+79995551111"));
        assertFalse(validator.isValideLogin("79995551111s"));
    }

    @Test
    public void cleanLogin(){
        assertEquals("79995551111", validator.cleanLogin("79995551111"));
        assertEquals("79995551111", validator.cleanLogin("+79995551111"));
        assertEquals("79995551111", validator.cleanLogin("7999-555-11-11"));
        assertEquals("79995551111", validator.cleanLogin("7999 555 11 11"));
        assertEquals("79995551111", validator.cleanLogin("+7999-555 11 11"));
    }
}
