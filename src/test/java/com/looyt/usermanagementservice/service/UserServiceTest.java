package com.looyt.usermanagementservice.service;


import com.looyt.usermanagementservice.dto.UserRequest;
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
    }

    @Test
    void createUser_success() {
        when(mapper.toEntity(request)).thenReturn(user);
        when(repo.save(user)).thenReturn(user);

        User result = service.create(request);

        assertEquals("John", result.getName());
        verify(repo, times(1)).save(user);
    }

    @Test
    void getById_found() {
        when(repo.findById(1L)).thenReturn(Optional.of(user));
        User result = service.getById(1L);
        assertEquals("John", result.getName());
    }

    @Test
    void getById_notFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getById(1L));
    }

    @Test
    void listPaged_success() {
        Page<User> page = new PageImpl<>(List.of(user));
        when(repo.findAll(PageRequest.of(0, 10, Sort.by("createdAt").descending()))).thenReturn(page);

        Page<User> result = service.listPaged(0, 10);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void update_success() {
        when(repo.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(mapper).updateEntityFromRequest(request, user);
        when(repo.save(user)).thenReturn(user);

        User result = service.update(1L, request);
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