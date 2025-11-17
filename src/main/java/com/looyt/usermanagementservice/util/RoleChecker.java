package com.looyt.usermanagementservice.util;

import com.looyt.usermanagementservice.exception.AccessDeniedException;
import com.looyt.usermanagementservice.model.User;
import com.looyt.usermanagementservice.model.UserRole;
import com.looyt.usermanagementservice.model.constants.ApiErrorMessage;

import java.util.Arrays;

public class RoleChecker {

    public static void requireRole(User user, UserRole requiredRole) {
        if (user.getRole() != requiredRole) {
            throw new AccessDeniedException(
                    ApiErrorMessage.ACCESS_DENIED_ROLE.getMessage(user.getUsername(), requiredRole)
            );
        }
    }

    public static void requireAnyRole(User user, UserRole... allowedRoles) {
        boolean hasRole = Arrays.asList(allowedRoles).contains(user.getRole());
        if (!hasRole) {
            throw new AccessDeniedException(
                    ApiErrorMessage.ACCESS_DENIED_ANY_ROLE.getMessage(
                            user.getUsername(), Arrays.toString(allowedRoles))
            );
        }
    }
}
