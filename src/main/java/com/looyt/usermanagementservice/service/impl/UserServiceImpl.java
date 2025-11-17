package com.looyt.usermanagementservice.service.impl;

import com.looyt.usermanagementservice.dto.UserRequest;
import com.looyt.usermanagementservice.dto.UserResponse;
import com.looyt.usermanagementservice.dto.PageResponse;
import com.looyt.usermanagementservice.exception.DataExistException;
import com.looyt.usermanagementservice.exception.NotFoundException;
import com.looyt.usermanagementservice.mapper.UserMapper;
import com.looyt.usermanagementservice.model.User;
import com.looyt.usermanagementservice.model.UserRole;
import com.looyt.usermanagementservice.model.constants.ApiErrorMessage;
import com.looyt.usermanagementservice.repository.UserRepository;
import com.looyt.usermanagementservice.service.UserService;
import com.looyt.usermanagementservice.util.RoleChecker;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final UserMapper mapper;

    public UserServiceImpl(UserRepository repo,
                           @Qualifier("userMapperImpl") UserMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public UserResponse create(@NotNull UserRequest req, User currentUser) {
        if (req.getRole() != null && req.getRole() != UserRole.USER) {
            RoleChecker.requireRole(currentUser, UserRole.ADMIN);
        }

        if (repo.existsByEmail(req.getEmail())) {
            throw new DataExistException(ApiErrorMessage.EMAIL_ALREADY_EXISTS.getMessage(req.getEmail()));
        }

        User user = mapper.toEntity(req);
        user = repo.save(user);
        return mapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByIdResponse(@NotNull Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(id)));
        return mapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> listPagedResponse(Pageable pageable) {
        Page<User> paged = repo.findAll(pageable);

        List<UserResponse> items = paged.map(mapper::toResponse).getContent();

        return PageResponse.<UserResponse>builder()
                .items(items)
                .page(paged.getNumber() + 1)
                .size(paged.getSize())
                .totalPages(paged.getTotalPages())
                .totalItems(paged.getTotalElements())
                .build();
    }


    @Override
    @Transactional
    public UserResponse update(@NotNull Long id, UserRequest req, User currentUser) {

        RoleChecker.requireAnyRole(currentUser, UserRole.ADMIN);

        User user = repo.findById(id)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(id)));

        if (!user.getEmail().equals(req.getEmail()) && repo.existsByEmail(req.getEmail())) {
            throw new DataExistException(ApiErrorMessage.EMAIL_ALREADY_EXISTS.getMessage(req.getEmail()));
        }

        mapper.updateUser(req, user);
        user = repo.save(user);
        return mapper.toResponse(user);
    }

    @Override
    @Transactional
    public void delete(@NotNull Long id, User currentUser) {
        RoleChecker.requireRole(currentUser, UserRole.ADMIN);

        User user = repo.findById(id)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(id)));
        repo.delete(user);
    }
    @Override
    @Transactional(readOnly = true)
    public User getByIdEntity(@NotNull Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(id)));
    }

}
