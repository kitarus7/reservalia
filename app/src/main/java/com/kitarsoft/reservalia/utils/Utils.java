package com.kitarsoft.reservalia.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.Timestamp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static  void ocultarTeclado(Context context, View v){
        InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static Timestamp getDateTime(int year, int month, int day, int hour, int minute){
        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, year);
        myCal.set(Calendar.MONTH, month-1);
        myCal.set(Calendar.DAY_OF_MONTH, day);
        myCal.set(Calendar.HOUR_OF_DAY, hour);
        myCal.set(Calendar.MINUTE, minute);
        Date date = myCal.getTime();
        Timestamp result = new Timestamp(date);
        return result;
    }

    public static String getDateFormated(Timestamp timestampDate){
        Date date = new Date(timestampDate.getSeconds()*1000);
        Calendar myCal = Calendar.getInstance();
        myCal.setTime(date);
        String fecha =
                    myCal.get(Calendar.DAY_OF_MONTH) + "/" +
                    myCal.get(Calendar.MONTH) + "/" +
                    myCal.get(Calendar.YEAR) + " " +
                    myCal.get(Calendar.HOUR_OF_DAY) + ":" +
                    myCal.get(Calendar.MINUTE);
        return fecha;
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
