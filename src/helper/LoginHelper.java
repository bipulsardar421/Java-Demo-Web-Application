package helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginHelper {
     public static String hashString(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algo not found", e);
        }
    }

    public static boolean compareHash(String normalText, String hashedText) {
        String generatedHash = hashString(normalText);
        return generatedHash.equalsIgnoreCase(hashedText);
    }
    public static void main(String[] args) {
        
    }
}
