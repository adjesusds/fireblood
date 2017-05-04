package com.arieldiax.codelab.fireblood.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Utils {

    /**
     * Creates a new Utils object (no, it won't).
     */
    private Utils() {
        // Required empty private constructor (to prevent instantiation).
    }

    /**
     * Generates the MD5 hash of the string.
     *
     * @param str The string.
     * @return The MD5 hash of the string.
     */
    public static String md5(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes(), 0, str.length());
            return new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }
        return "";
    }

    /**
     * Generates the epoch time of the string.
     *
     * @param str         The string.
     * @param datePattern Pattern of the date.
     * @return The epoch time of the string.
     */
    public static long epochTime(String str, String datePattern) {
        Date date = null;
        try {
            date = new SimpleDateFormat(datePattern, Locale.getDefault()).parse(str);
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
        if (date != null) {
            return date.getTime();
        }
        return 0;
    }
}
