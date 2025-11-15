package com.looyt.usermanagementservice.service.impl;

import com.looyt.usermanagementservice.dto.UserRequest;
import com.looyt.usermanagementservice.dto.UserResponse;
import com.looyt.usermanagementservice.dto.PageResponse;
import com.looyt.usermanagementservice.exception.NotFoundException;
import com.looyt.usermanagementservice.mapper.UserMapper;
import com.looyt.usermanagementservice.model.User;
import com.looyt.usermanagementservice.repository.UserRepository;
import com.looyt.usermanagementservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final UserMapper mapper;

    public UserServiceImpl(UserRepository repo,
                           @Qualifier("userMapperImpl") UserMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public UserResponse create(UserRequest req) {
        User user = mapper.toEntity(req);
        user = repo.save(user);
        return mapper.toResponse(user);
    }

    public UserResponse getByIdResponse(Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        return mapper.toResponse(user);
    }

    public PageResponse<UserResponse> listPagedResponse(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> paged = repo.findAll(pageable);

        List<UserResponse> items = paged.getContent().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return PageResponse.<UserResponse>builder()
                .items(items)
                .page(paged.getNumber())
                .size(paged.getSize())
                .totalPages(paged.getTotalPages())
                .totalItems(paged.getTotalElements())
                .build();
    }

    public UserResponse update(Long id, UserRequest req) {
        User user = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        mapper.updateEntityFromRequest(req, user);
        user = repo.save(user);
        return mapper.toResponse(user);
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new NotFoundException("User not found: " + id);
        repo.deleteById(id);
    }
}
