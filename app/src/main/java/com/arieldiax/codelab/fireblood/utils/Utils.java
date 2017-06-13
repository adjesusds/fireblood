package com.arieldiax.codelab.fireblood.utils;

import android.text.format.DateUtils;

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
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            byte[] bytes = messageDigest.digest();
            StringBuilder hashStringBuilder = new StringBuilder();
            for (byte bit : bytes) {
                hashStringBuilder.append(String.format(Locale.getDefault(), "%02X", bit));
            }
            return hashStringBuilder.toString().toLowerCase();
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }
        return "";
    }

    /**
     * Generates the Unix time of the date.
     *
     * @param dateString  String of the date.
     * @param datePattern Pattern of the date.
     * @return The Unix time of the date.
     */
    public static long unixTime(
            String dateString,
            String datePattern
    ) {
        Date date = null;
        try {
            date = new SimpleDateFormat(datePattern, Locale.getDefault()).parse(dateString);
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
        if (date != null) {
            return date.getTime();
        }
        return 0;
    }

    /**
     * Calculates the age of a user.
     *
     * @param birthdayEpochTime Epoch time of the birthday.
     * @return The age of a user.
     */
    public static int calculateUserAge(long birthdayEpochTime) {
        return (int) ((System.currentTimeMillis() - birthdayEpochTime) / DateUtils.YEAR_IN_MILLIS);
    }

    /**
     * Formats the date.
     *
     * @param unixTime    Time in Unix measurement.
     * @param datePattern Pattern of the date.
     * @return The date formatted.
     */
    public static String formatDate(
            long unixTime,
            String datePattern
    ) {
        return (new SimpleDateFormat(datePattern, Locale.getDefault())).format(new Date(unixTime));
    }
}
