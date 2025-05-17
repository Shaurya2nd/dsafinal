package com.langcen.util;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {
    private static final int SALT_LENGTH_BYTE = 16;  // 16 bytes
    private static final int HASH_LENGTH = 256;     // 256 bits
    private static final int ITERATION_COUNT = 65536;

    // Generate a random salt
    public static byte[] getRandomSalt() {
        byte[] salt = new byte[SALT_LENGTH_BYTE];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    // Generate a hashed password with a salt
    public static String hashPassword(String password) {
        try {
            // Generate a random salt
            byte[] salt = getRandomSalt();

            // Hash the password
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, HASH_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            // Combine salt and hash, separated by a colon
            return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (Exception ex) {
            throw new RuntimeException("Error while hashing password", ex);
        }
    }

    // Validate the password by comparing the hash values
    public static boolean validatePassword(String inputPassword, String storedHash) {
        try {
            // Split the stored hash into salt and hash parts
            String[] parts = storedHash.split(":");
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            String inputHash = hashPasswordWithSalt(inputPassword, salt);
            return inputHash.equals(storedHash);
        } catch (Exception ex) {
            return false;
        }
    }

    // Hash the password using an existing salt
    private static String hashPasswordWithSalt(String password, byte[] salt) throws Exception {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, HASH_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }
}