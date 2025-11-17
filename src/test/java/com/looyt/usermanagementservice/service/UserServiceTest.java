package com.looyt.usermanagementservice.service;

import com.looyt.usermanagementservice.dto.PageResponse;
import com.looyt.usermanagementservice.dto.UserRequest;
import com.looyt.usermanagementservice.dto.UserResponse;
import com.looyt.usermanagementservice.exception.DataExistException;
import com.looyt.usermanagementservice.exception.NotFoundException;
import com.looyt.usermanagementservice.mapper.UserMapper;
import com.looyt.usermanagementservice.model.User;
import com.looyt.usermanagementservice.model.UserRole;
import com.looyt.usermanagementservice.repository.UserRepository;
import com.looyt.usermanagementservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.OffsetDateTime;
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
    private UserServiceImpl service;

    private User user;
    private UserRequest request;
    private UserResponse response;

    private User currentUser;

    @BeforeEach
    void setup() {
        request = new UserRequest("john", "123456", "john@example.com", "+123456789", UserRole.USER);

        user = User.builder()
                .id(1L)
                .username("john")
                .password("123456")
                .email("john@example.com")
                .phone("+123456789")
                .role(UserRole.USER)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        response = UserResponse.builder()
                .id(1L)
                .username("john")
                .email("john@example.com")
                .phone("+123456789")
                .role(UserRole.USER)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        currentUser = User.builder()
                .id(99L)
                .username("admin")
                .email("admin@example.com")
                .password("adminpass")
                .phone("+987654321")
                .role(UserRole.ADMIN)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }

    @Test
    void createUser_success() {
        when(repo.existsByEmail(request.getEmail())).thenReturn(false);
        when(mapper.toEntity(request)).thenReturn(user);
        when(repo.save(user)).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(response);

        UserResponse result = service.create(request, currentUser);
        assertNotNull(result);
        assertEquals("john", result.getUsername());
        assertEquals("+123456789", result.getPhone());
        verify(repo).save(user);
    }

    @Test
    void createUser_emailExists() {
        when(repo.existsByEmail(request.getEmail())).thenReturn(true);
        assertThrows(DataExistException.class, () -> service.create(request, currentUser));
    }

    @Test
    void getById_found() {
        when(repo.findById(1L)).thenReturn(Optional.of(user));
        when(mapper.toResponse(user)).thenReturn(response);

        UserResponse result = service.getByIdResponse(1L);
        assertNotNull(result);
        assertEquals("john", result.getUsername());
        assertEquals("+123456789", result.getPhone());
    }

    @Test
    void getById_notFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getByIdResponse(1L));
    }

    @Test
    void listPaged_success() {
        Page<User> page = new PageImpl<>(List.of(user));
        Pageable pageable = PageRequest.of(0, 10);

        when(repo.findAll(pageable)).thenReturn(page);
        when(mapper.toResponse(user)).thenReturn(response);

        PageResponse<UserResponse> result = service.listPagedResponse(pageable);
        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals("john", result.getItems().get(0).getUsername());
        assertEquals("+123456789", result.getItems().get(0).getPhone());
    }

    @Test
    void update_success() {
        when(repo.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(mapper).updateUser(request, user);
        when(repo.save(user)).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(response);

        UserResponse result = service.update(1L, request, currentUser);
        assertNotNull(result);
        assertEquals("john", result.getUsername());
        assertEquals("+123456789", result.getPhone());
    }

    @Test
    void delete_success() {
        when(repo.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(repo).delete(user);

        service.delete(1L, currentUser);
        verify(repo).delete(user);
    }

    @Test
    void delete_notFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.delete(1L, currentUser));
    }
}
