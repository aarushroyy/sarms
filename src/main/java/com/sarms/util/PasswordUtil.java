// src/main/java/com/sarms/util/PasswordUtil.java
package com.sarms.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {
    private static final int SALT_LENGTH = 16;

    /**
     * Generates a random salt for password hashing
     * @return a random salt as a Base64-encoded string
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hashes the given password using SHA-256 algorithm
     * @param password the password to hash
     * @param salt the salt to use for hashing
     * @return the hashed password as a Base64-encoded string
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Base64.getDecoder().decode(salt));
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Verifies if the given password matches the hashed password
     * @param password the password to verify
     * @param salt the salt used for hashing
     * @param hashedPassword the hashed password to compare against
     * @return true if the password is correct, false otherwise
     */
    public static boolean verifyPassword(String password, String salt, String hashedPassword) {
        String hashedInput = hashPassword(password, salt);
        return hashedInput.equals(hashedPassword);
    }
}