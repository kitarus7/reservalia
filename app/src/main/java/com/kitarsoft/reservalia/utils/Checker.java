package com.kitarsoft.reservalia.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Checker {

    public static boolean email(String email){

        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public static boolean phone(String phone){

        String regexMobile = "(\\+34|0034|34)?[ -]*(6|7)[ -]*([0-9][ -]*){8}";
        String regexPhone = "(\\+34|0034|34)?[ -]*(8|9)[ -]*([0-9][ -]*){8}";

        Pattern pattern = Pattern.compile(regexMobile);
        Matcher matcher = pattern.matcher(phone);

        if(matcher.matches())
            return true;
        else{
            pattern = Pattern.compile(regexPhone);
            matcher = pattern.matcher(phone);
        }

        if(matcher.matches())
            return true;
        else
            return false;
    }

    public static boolean password(String password){

        String regex = "^(?=\\w*\\d)\\S{6,16}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();

    }

}
