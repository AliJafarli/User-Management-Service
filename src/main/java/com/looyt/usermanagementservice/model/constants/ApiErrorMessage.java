package com.looyt.usermanagementservice.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMessage {
    USER_NOT_FOUND_BY_ID("User with ID: %s was not found"),
    USERNAME_ALREADY_EXISTS("Username: %s already exists"),
    USERNAME_NOT_FOUND("Username: %s was not found"),
    EMAIL_ALREADY_EXISTS("Email: %s already exists"),
    EMAIL_NOT_FOUND("Email: %s was not found"),
    ACCESS_DENIED_ROLE("Access denied: user '%s' must have role %s"),
    ACCESS_DENIED_ANY_ROLE("Access denied: user '%s' must have one of roles %s")
    ;


    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
