package com.github.spikevlg.balanceofphone.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="person")
@XmlAccessorType(XmlAccessType.FIELD)
public class PhoneUser {

    @XmlElement(required = true)
    String user;

    @XmlElement
    String password;

    public PhoneUser(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public PhoneUser(){}

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    class PasswordUtils {
        private final int MIN_LENGTH_PASSWORD = 10;
        private final int MAX_LENGTH_PASSWORD = 100;

        /**
         * Validates password
         * @param pass - password for validate
         * @return null if password validated, error message if password wrong.
         */
        public String validatePassword(String pass) {
            if (pass == null) {
                throw new IllegalArgumentException("Password cannot be null!");
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
            if (!hasLowercase){
                resultMessage.append("Password needs lower case characters.\n");
            }

            boolean hasNumber = pass.matches(".*\\d.*");
            if (!hasNumber){
                resultMessage.append("Password needs digit characters.\n");
            }

            boolean noSpecialChar = pass.matches("[a-zA-Z0-9 ]*");
            if (noSpecialChar) {
                resultMessage.append("Password needs a special character i.e. !,@,#, etc.\n");
            }

            return resultMessage.toString();
        }


    }
}
