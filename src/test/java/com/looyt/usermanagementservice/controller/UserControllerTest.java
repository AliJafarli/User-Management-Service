package com.looyt.usermanagementservice.controller;

import com.looyt.usermanagementservice.dto.PageResponse;
import com.looyt.usermanagementservice.dto.UserRequest;
import com.looyt.usermanagementservice.dto.UserResponse;
import com.looyt.usermanagementservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

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

    private UserResponse response;
    private UserRequest request;

    @BeforeEach
    void setup() {
        request = new UserRequest("John", "john@example.com", "123456", null);

        response = UserResponse.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .phone("123456")
                .role(null)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }

    @Test
    void create_success() {
        when(service.create(request)).thenReturn(response);

        var result = controller.create(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("John", result.getBody().getName());
    }

    @Test
    void get_success() {
        when(service.getByIdResponse(1L)).thenReturn(response);

        var result = controller.get(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("John", result.getBody().getName());
    }

    @Test
    void list_success() {
        PageResponse<UserResponse> pageResponse = PageResponse.<UserResponse>builder()
                .items(List.of(response))
                .page(0)
                .size(10)
                .totalPages(1)
                .totalItems(1)
                .build();

        when(service.listPagedResponse(0, 10)).thenReturn(pageResponse);

        var result = controller.list(0, 10);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().getItems().size());
        assertEquals("John", result.getBody().getItems().get(0).getName());
    }

    @Test
    void update_success() {
        when(service.update(1L, request)).thenReturn(response);

        var result = controller.update(1L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("John", result.getBody().getName());
    }

    @Test
    void delete_success() {
        doNothing().when(service).delete(1L);

        var result = controller.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void health_check() {
        var result = controller.health();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("OK", result.getBody());
    }
}
