package com.looyt.usermanagementservice.service;

import com.looyt.usermanagementservice.dto.PageResponse;
import com.looyt.usermanagementservice.dto.UserRequest;
import com.looyt.usermanagementservice.dto.UserResponse;


public interface UserService {

    UserResponse create(UserRequest req);
    UserResponse getByIdResponse(Long id);
    UserResponse update(Long id, UserRequest req);
    void delete(Long id);
    PageResponse<UserResponse> listPagedResponse(int page, int size);
}
