package com.looyt.usermanagementservice.controller;

import com.looyt.usermanagementservice.dto.PageResponse;
import com.looyt.usermanagementservice.dto.UserRequest;
import com.looyt.usermanagementservice.dto.UserResponse;
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
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    private UserRequest request;
    private UserResponse response;
    private User currentUser;

    @BeforeEach
    void setup() {
        request = new UserRequest("john", "123456", "john@example.com", "+123456789", UserRole.USER);

        response = UserResponse.builder()
                .id(1L)
                .username("john")
                .email("john@example.com")
                .phone("+123456789")
                .role(UserRole.USER)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        currentUser = User.builder()
                .id(99L)
                .username("admin")
                .email("admin@example.com")
                .password("adminpass")
                .role(UserRole.ADMIN)
                .phone("+987654321")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }

    @Test
    void create_success() {
        when(service.getByIdEntity(99L)).thenReturn(currentUser);
        when(service.create(request, currentUser)).thenReturn(response);

        ResponseEntity<UserResponse> result = controller.create(request, 99L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("john", result.getBody().getUsername());
        assertEquals("+123456789", result.getBody().getPhone());
    }

    @Test
    void get_success() {
        when(service.getByIdResponse(1L)).thenReturn(response);

        ResponseEntity<UserResponse> result = controller.get(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("john", result.getBody().getUsername());
        assertEquals("+123456789", result.getBody().getPhone());
    }

    @Test
    void list_success() {
        PageResponse<UserResponse> pageResponse = PageResponse.<UserResponse>builder()
                .items(List.of(response))
                .page(1)
                .size(10)
                .totalPages(1)
                .totalItems(1)
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        when(service.listPagedResponse(pageable)).thenReturn(pageResponse);

        ResponseEntity<PageResponse<UserResponse>> result = controller.list(0, 10, "createdAt,desc");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().getItems().size());
        assertEquals("+123456789", result.getBody().getItems().get(0).getPhone());
    }

    @Test
    void update_success() {
        when(service.getByIdEntity(99L)).thenReturn(currentUser);
        when(service.update(1L, request, currentUser)).thenReturn(response);

        ResponseEntity<UserResponse> result = controller.update(1L, request, 99L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("john", result.getBody().getUsername());
        assertEquals("+123456789", result.getBody().getPhone());
    }

    @Test
    void delete_success() {
        when(service.getByIdEntity(99L)).thenReturn(currentUser);
        doNothing().when(service).delete(1L, currentUser);

        ResponseEntity<Void> result = controller.delete(1L, 99L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void health_check() {
        ResponseEntity<String> result = controller.health();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("OK", result.getBody());
    }
}
