package com.github.spikevlg.balanceofphone.utils;

import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Utils class for validate data.
 */
@Component
public class DataValidator {
    /**
     * Min length of password
     */
    private static final int MIN_LENGTH_PASSWORD = 10;
    /**
     * Max length of password
     */
    private static final int MAX_LENGTH_PASSWORD = 100;

    /**
     * Validate password.
     * @param password of user
     * @return error message
     */
    public String isValidPassword(String password){
        Objects.requireNonNull(password, "phoneLogin cannot be null");

        StringBuilder resultMessage = new StringBuilder();
        if (password.length() < MIN_LENGTH_PASSWORD) {
            resultMessage.append("Password cannot be less " + MIN_LENGTH_PASSWORD + " characters.\n");
        }
        if (password.length() > MAX_LENGTH_PASSWORD) {
            resultMessage.append("Password cannot be great " + MAX_LENGTH_PASSWORD + " characters.\n");
        }

        boolean hasUppercase = !password.equals(password.toLowerCase());
        if (!hasUppercase) {
            resultMessage.append("Password needs upper case characters.\n");
        }

        boolean hasLowercase = !password.equals(password.toUpperCase());
        if (!hasLowercase) {
            resultMessage.append("Password needs lower case characters.\n");
        }

        boolean hasNumber = password.matches(".*\\d.*");
        if (!hasNumber) {
            resultMessage.append("Password needs digit characters.\n");
        }

        boolean noSpecialChar = password.matches("[a-zA-Z0-9 ]*");
        if (noSpecialChar) {
            resultMessage.append("Password needs a special character i.e. !,@,#, etc.\n");
        }
        return resultMessage.toString();
    }

    /**
     * Validates password.
     * @param password - password for validate
     * @throws WeakPasswordException if password too weak
     */
    public void validatePassword(String password) throws WeakPasswordException {
        Objects.requireNonNull(password, "phoneLogin cannot be null");
        String resultMessage = isValidPassword(password);
        if (resultMessage.length() != 0) {
            throw new WeakPasswordException(resultMessage.toString());
        }
    }

    /**
     * Checks phone number.
     * @param phoneNumber of user
     * @return result of validations
     */
    public boolean isValidPhoneNumber(String phoneNumber){
        Objects.requireNonNull(phoneNumber, "phoneNumber cannot be null");
        return phoneNumber.matches("\\+?\\d[\\d -]{5,20}\\d");
    }

    /**
     * Validate login. Login is phone number.
     * @param phoneNumber user login
     * @throws InvalidLoginException if login is not phone number
     */
    public void validatePhoneNumber(String phoneNumber) throws InvalidLoginException{
        Objects.requireNonNull(phoneNumber, "phoneNumber cannot be null");
        if (!isValidPhoneNumber(phoneNumber)){
            throw new InvalidLoginException("Invalid login. '" + phoneNumber + "' is not phone number");
        }
    }

    /**
     * Delete special characters from login.
     * Login is phone number. Phone number can contain characters ' ', '+', '-'.
     * @param dirtyPhoneNumber user login from request
     * @throws InvalidLoginException if login is not phone number
     * @return phone number in number representation
     */
    public String getPurePhoneNumber(String dirtyPhoneNumber) throws InvalidLoginException{
        Objects.requireNonNull(dirtyPhoneNumber, "dirtyPhoneNumber must be not null");
        validatePhoneNumber(dirtyPhoneNumber);
        return dirtyPhoneNumber.replaceAll("-|\\+| ", "");
    }
}