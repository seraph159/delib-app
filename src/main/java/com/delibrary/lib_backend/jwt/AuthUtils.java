package com.delibrary.lib_backend.jwt;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    // Utility method to get the authenticated client's email
    public static String getAuthenticatedClientEmail() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName(); // This retrieves the username or email from the JWT
    }
}
