package io.github.ezequiellima10.fintech.user.mapper;

import io.github.ezequiellima10.fintech.user.dto.UserRequestDTO;
import io.github.ezequiellima10.fintech.user.dto.UserResponseDTO;
import io.github.ezequiellima10.fintech.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User requestToEntity(UserRequestDTO dto) {
       return User.builder()
                .username(dto.username())
                .email(dto.email())
                .build();
    }

    public UserResponseDTO entityToResponseDTO(User entity) {
        return UserResponseDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
