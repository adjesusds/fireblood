package com.arieldiax.codelab.fireblood.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Utils {

    /**
     * Creates a new Utils object (no, it won't).
     */
    private Utils() {
        // Required empty private constructor (to prevent instantiation).
    }

    /**
     * Gets the MD5 hash of the string.
     *
     * @param hashString String of the hash.
     * @return The MD5 hash of the string.
     */
    public static String getMd5Hash(String hashString) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(hashString.getBytes(), 0, hashString.length());
            return new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }
        return "";
    }
}
