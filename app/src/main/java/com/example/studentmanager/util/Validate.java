package com.example.studentmanager.util;

/**
 * Class Validate Validate fields of the AddStudent
 * name Field should be a String
 * rollNumber should be between 0 and 9999
 * class should be between 0 and 13
 *
 */
public class Validate {

    /**
     * stringValidate function validate string
     * string should not have any special characters
     *
     *
     * @param string
     * @return true is valid string
     */

    public boolean stringValidate(String string){
        if (!string.trim().equals("")) {

            String regx = "^[\\p{L} .'-]+$";
            if(string.matches(regx)){
                return  true;
            }
        }

        return false;
    }

    /**
     * validate rollNumber to be between 0 and 9999
     *
     * @param number
     * @return true if valid
     */

    public boolean rollNumberValidate(int number){
        return number < 9999 && number > 0;
    }

    /**
     * validate class to be between 0 and 13
     *
     * @param number
     * @return true of valid
     */

    public boolean classValidate(int number) {
        return number < 13 && number > 0;
    }
}
