package com.ishoal.core.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurePassword {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static final SecurePassword PROTECTED = new SecurePassword("");

    private final String hashedPassword;

    private SecurePassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public static SecurePassword fromClearText(String clearTextPassword) {
        return new SecurePassword(passwordEncoder.encode(clearTextPassword));
    }

    public static SecurePassword fromBCryptHash(String hashedPassword) {
        return new SecurePassword(hashedPassword);
    }

    public String getValue() {
        return hashedPassword;
    }

    @Override
    public String toString() {
        return "[Protected]";
    }
}
