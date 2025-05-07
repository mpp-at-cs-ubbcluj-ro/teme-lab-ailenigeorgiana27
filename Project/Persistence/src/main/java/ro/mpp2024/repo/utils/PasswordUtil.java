package ro.mpp2024.repo.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    // Hashing a password
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12)); // Factorul 12 e un echilibru bun între securitate și performanță
    }

    // Verifying a password
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
