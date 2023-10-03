package com.farneser.weatherviewer.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

public abstract class PasswordUtil {
    private static final Logger LOGGER = Logger.getLogger(PasswordUtil.class.getName());

    public static String hashPassword(String password) {
        try {
            var md = MessageDigest.getInstance("SHA-512");

            var passwordBytes = password.getBytes();
            var hashBytes = md.digest(passwordBytes);
            var bigInt = new BigInteger(1, hashBytes);
            var hashedPassword = new StringBuilder(bigInt.toString(16));

            while (hashedPassword.length() < 64) {
                hashedPassword.insert(0, "0");
            }

            return hashedPassword.toString();

        } catch (NoSuchAlgorithmException e) {

            LOGGER.warning(Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    public static boolean isPasswordCorrect(String password, String hash) {
        return Objects.equals(hashPassword(password), hash);
    }
}
