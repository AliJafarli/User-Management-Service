package com.looyt.usermanagementservice.controller;

import com.looyt.usermanagementservice.dto.PageResponse;
import com.looyt.usermanagementservice.dto.UserRequest;
import com.looyt.usermanagementservice.dto.UserResponse;
import com.looyt.usermanagementservice.mapper.UserMapper;
import com.looyt.usermanagementservice.model.User;
import com.looyt.usermanagementservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final UserMapper mapper;

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest req) {
        User user = service.create(req);
        return ResponseEntity.created(URI.create("/api/users/" + user.getId()))
                .body(mapper.toResponse(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.getById(id)));
    }

    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var paged = service.listPaged(page, size);
        var items = paged.getContent().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                PageResponse.<UserResponse>builder()
                        .items(items)
                        .page(paged.getNumber())
                        .size(paged.getSize())
                        .totalPages(paged.getTotalPages())
                        .totalItems(paged.getTotalElements())
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UserRequest req) {
        return ResponseEntity.ok(mapper.toResponse(service.update(id, req)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
