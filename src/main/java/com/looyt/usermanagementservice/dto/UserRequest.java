package com.looyt.usermanagementservice.dto;


import com.looyt.usermanagementservice.model.UserRole;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "Username cannot be empty")
    @Size(max = 30)
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(max = 50)
    private String password;

    @NotBlank(message = "Email cannot be empty")
    @Size(max = 50)
    private String email;

    @Size(max = 20)
    private String phone;

    private UserRole role;
}

