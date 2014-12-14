package com.github.spikevlg.balanceofphone.utils;

import org.springframework.stereotype.Component;

/**
 * Utils class for validate data.
 */
@Component
public class DataValidator {
    private static final int MIN_LENGTH_PASSWORD = 10;
    private static final int MAX_LENGTH_PASSWORD = 100;

    /**
     * Validates password
     *
     * @param pass - password for validate
     * @return null if password validated, error message if password wrong.
     */
    public String validatePassword(String pass) {
        if (pass == null) {
            return "Password cannot be null!";
        }
        StringBuilder resultMessage = new StringBuilder();
        if (pass.length() < MIN_LENGTH_PASSWORD) {
            resultMessage.append("Password cannot be less " + MIN_LENGTH_PASSWORD + " characters.\n");
        }
        if (pass.length() > MAX_LENGTH_PASSWORD) {
            resultMessage.append("Password cannot be great " + MAX_LENGTH_PASSWORD + " characters.\n");
        }

        boolean hasUppercase = !pass.equals(pass.toLowerCase());
        if (!hasUppercase) {
            resultMessage.append("Password needs upper case characters.\n");
        }

        boolean hasLowercase = !pass.equals(pass.toUpperCase());
        if (!hasLowercase) {
            resultMessage.append("Password needs lower case characters.\n");
        }

        boolean hasNumber = pass.matches(".*\\d.*");
        if (!hasNumber) {
            resultMessage.append("Password needs digit characters.\n");
        }

        boolean noSpecialChar = pass.matches("[a-zA-Z0-9 ]*");
        if (noSpecialChar) {
            resultMessage.append("Password needs a special character i.e. !,@,#, etc.\n");
        }
        if (resultMessage.length() == 0) {
            return null;
        }
        else{
            return resultMessage.toString();
        }
    }

    /**
     * Validate login. Login represent phone number.
     * @param phoneLogin user login
     * @return false if login invalid
     */
    public boolean isValideLogin(String phoneLogin){
        if (phoneLogin == null){
            return false;
        }
        return phoneLogin.matches("\\+?\\d[\\d -]{5,20}\\d");
    }

    /**
     * Delete special characters from login.
     * @param dirtyLogin user login from request
     * @return login without special characters
     */
    public String cleanLogin(String dirtyLogin){
        if (dirtyLogin == null){
            throw new IllegalArgumentException("dirtyLogin must be not null");
        }
        return dirtyLogin.replaceAll("-|\\+| ", "");
    }
}