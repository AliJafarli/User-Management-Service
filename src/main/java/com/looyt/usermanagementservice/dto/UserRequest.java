package com.looyt.usermanagementservice.dto;


import com.looyt.usermanagementservice.model.UserRole;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank
    @Size(max = 200)
    private String name;

    @NotBlank
    @Email
    private String email;

    @Size(max = 50)
    private String phone;

    private UserRole role;
}

