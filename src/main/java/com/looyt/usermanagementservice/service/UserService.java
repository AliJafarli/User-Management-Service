package com.looyt.usermanagementservice.service;

import com.looyt.usermanagementservice.dto.UserRequest;
import com.looyt.usermanagementservice.model.User;
import org.springframework.data.domain.Page;

public interface UserService {

    User create(UserRequest req);
    User getById(Long id);
    User update(Long id, UserRequest req);
    void delete(Long id);
    Page<User> listPaged(int page, int size);
}
