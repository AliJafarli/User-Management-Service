package com.looyt.usermanagementservice.service;


import com.looyt.usermanagementservice.dto.PageResponse;
import com.looyt.usermanagementservice.dto.UserRequest;
import com.looyt.usermanagementservice.dto.UserResponse;
import com.looyt.usermanagementservice.exception.NotFoundException;
import com.looyt.usermanagementservice.mapper.UserMapper;
import com.looyt.usermanagementservice.model.User;
import com.looyt.usermanagementservice.model.UserRole;
import com.looyt.usermanagementservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repo;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService service;

    private User user;
    private UserRequest request;
    private UserResponse response;

    @BeforeEach
    void setup() {
        request = new UserRequest("John", "john@example.com", "123456", UserRole.USER);
        user = User.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .phone("123456")
                .role(UserRole.USER)
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
    void createUser_success() {
        when(mapper.toEntity(request)).thenReturn(user);
        when(repo.save(user)).thenReturn(user);

        UserResponse result = service.create(request);

        assertEquals("John", result.getName());
        verify(repo, times(1)).save(user);
    }

    @Test
    void getById_found() {
        when(repo.findById(1L)).thenReturn(Optional.of(user));
        UserResponse result = service.getByIdResponse(1L);
        assertEquals("John", result.getName());
    }

    @Test
    void getById_notFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getByIdResponse(1L));
    }

    @Test
    void listPaged_success() {
        Page<User> page = new PageImpl<>(List.of(user));
        when(repo.findAll(PageRequest.of(0, 10, Sort.by("createdAt").descending()))).thenReturn(page);
        when(mapper.toResponse(user)).thenReturn(response);

        PageResponse<UserResponse> result = service.listPagedResponse(0, 10);

        assertEquals(1, result.getItems().size());
        assertEquals("John", result.getItems().get(0).getName());
    }


    @Test
    void update_success() {
        when(repo.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(mapper).updateEntityFromRequest(request, user);
        when(repo.save(user)).thenReturn(user);

        UserResponse result = service.update(1L, request);
        assertEquals("John", result.getName());
    }

    @Test
    void delete_success() {
        when(repo.existsById(1L)).thenReturn(true);
        doNothing().when(repo).deleteById(1L);

        service.delete(1L);

        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void delete_notFound() {
        when(repo.existsById(1L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> service.delete(1L));
    }
}