package com.looyt.usermanagementservice.controller;

import com.looyt.usermanagementservice.dto.UserRequest;
import com.looyt.usermanagementservice.dto.UserResponse;
import com.looyt.usermanagementservice.mapper.UserMapper;
import com.looyt.usermanagementservice.model.User;
import com.looyt.usermanagementservice.model.UserRole;
import com.looyt.usermanagementservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService service;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserController controller;

    private User user;
    private UserResponse response;
    private UserRequest request;

    @BeforeEach
    void setup() {
        request = new UserRequest("John", "john@example.com", "123456", UserRole.USER);
        user = User.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .phone("123456")
                .role(UserRole.USER)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        response = UserResponse.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .phone("123456")
                .role(UserRole.USER)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Test
    void create_success() {
        when(service.create(request)).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(response);

        var result = controller.create(request);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("John", result.getBody().getName());
    }

    @Test
    void get_success() {
        when(service.getById(1L)).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(response);

        var result = controller.get(1L);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals("John", result.getBody().getName());
    }

    @Test
    void list_success() {
        Page<User> page = new PageImpl<>(List.of(user));
        when(service.listPaged(0, 10)).thenReturn(page);
        when(mapper.toResponse(user)).thenReturn(response);

        var result = controller.list(0, 10);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().getItems().size());
    }

    @Test
    void update_success() {
        when(service.update(1L, request)).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(response);

        var result = controller.update(1L, request);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals("John", result.getBody().getName());
    }

    @Test
    void delete_success() {
        doNothing().when(service).delete(1L);

        var result = controller.delete(1L);
        assertEquals(204, result.getStatusCode().value());
    }

    @Test
    void health_check() {
        var result = controller.health();
        assertEquals(200, result.getStatusCode().value());
        assertEquals("OK", result.getBody());
    }
}

