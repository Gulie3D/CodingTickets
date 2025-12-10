package org.example.codingtickets.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // plus le coût est élevé, plus c'est sécurisé, mais plus c'est lent
    private static final int WORKLOAD = 12;

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(WORKLOAD));
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2")) {
            throw new IllegalArgumentException("Mot de passe hashé invalide");
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
