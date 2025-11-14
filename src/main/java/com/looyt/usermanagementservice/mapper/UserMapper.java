package com.looyt.usermanagementservice.mapper;

import com.looyt.usermanagementservice.dto.UserRequest;
import com.looyt.usermanagementservice.dto.UserResponse;
import com.looyt.usermanagementservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequest request);

    UserResponse toResponse(User user);

    void updateEntityFromRequest(UserRequest request, @MappingTarget User user);
}

