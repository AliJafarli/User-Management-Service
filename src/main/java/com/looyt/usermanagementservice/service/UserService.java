package com.looyt.usermanagementservice.service;

import com.looyt.usermanagementservice.dto.PageResponse;
import com.looyt.usermanagementservice.dto.UserRequest;
import com.looyt.usermanagementservice.dto.UserResponse;
import com.looyt.usermanagementservice.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;


public interface UserService {

    UserResponse create(@NotNull UserRequest req, User currentUser);
    UserResponse getByIdResponse(Long id);
    UserResponse update(@NotNull Long id, UserRequest req, User currentUser);
    void delete(@NotNull Long id, User currentUser);
    PageResponse<UserResponse> listPagedResponse(Pageable pageable);
    User getByIdEntity(@NotNull Long id);
}
