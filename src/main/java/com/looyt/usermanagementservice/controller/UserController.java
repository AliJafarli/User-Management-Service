package com.looyt.usermanagementservice.controller;

import com.looyt.usermanagementservice.dto.PageResponse;
import com.looyt.usermanagementservice.dto.UserRequest;
import com.looyt.usermanagementservice.dto.UserResponse;
import com.looyt.usermanagementservice.model.User;
import com.looyt.usermanagementservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest req,
                                               @RequestHeader("X-User-Id") Long currentUserId) {
        log.info("User [{}] is attempting to create a new user with username [{}]", currentUserId, req.getUsername());
        User currentUser = service.getByIdEntity(currentUserId);
        UserResponse response = service.create(req, currentUser);
        log.info("User [{}] successfully created user [{}]", currentUserId, response.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable Long id) {
        log.info("Fetching user with id [{}]", id);
        UserResponse response = service.getByIdResponse(id);
        log.info("Fetched user [{}] successfully", response.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        log.info("Listing users: page [{}], size [{}], sort [{}]", page, size, sort);
        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));
        PageResponse<UserResponse> response = service.listPagedResponse(pageable);
        log.info("Listed [{}] users", response.getItems().size());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest req,
            @RequestHeader("X-User-Id") Long currentUserId
    ) {
        log.info("User [{}] attempting to update user [{}]", currentUserId, id);
        User currentUser = service.getByIdEntity(currentUserId);
        UserResponse response = service.update(id, req, currentUser);
        log.info("User [{}] successfully updated user [{}]", currentUserId, response.getUsername());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long currentUserId
    ) {
        log.info("User [{}] attempting to delete user [{}]", currentUserId, id);
        User currentUser = service.getByIdEntity(currentUserId);
        service.delete(id, currentUser);
        log.info("User [{}] successfully deleted user [{}]", currentUserId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        log.info("Health check endpoint called");
        return ResponseEntity.ok("OK");
    }
}
