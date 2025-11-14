package com.looyt.usermanagementservice.service.impl;

import com.looyt.usermanagementservice.dto.UserRequest;
import com.looyt.usermanagementservice.exception.NotFoundException;
import com.looyt.usermanagementservice.mapper.UserMapper;
import com.looyt.usermanagementservice.model.User;
import com.looyt.usermanagementservice.repository.UserRepository;
import com.looyt.usermanagementservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final UserMapper mapper;

    public User create(UserRequest req) {
        User user = mapper.toEntity(req);
        return repo.save(user);
    }

    public User getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    public Page<User> listPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return repo.findAll(pageable);
    }

    public User update(Long id, UserRequest req) {
        User user = getById(id);
        mapper.updateEntityFromRequest(req, user);
        return repo.save(user);
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new NotFoundException("User not found: " + id);
        repo.deleteById(id);
    }
}

