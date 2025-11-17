package com.looyt.usermanagementservice.dto;

import com.looyt.usermanagementservice.model.UserRole;
import lombok.*;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private UserRole role;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}

