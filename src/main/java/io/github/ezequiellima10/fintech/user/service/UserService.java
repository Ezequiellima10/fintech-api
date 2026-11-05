package io.github.ezequiellima10.fintech.user.service;

import io.github.ezequiellima10.fintech.shared.dto.ResponsePageDTO;
import io.github.ezequiellima10.fintech.user.dto.UserRequestDTO;
import io.github.ezequiellima10.fintech.user.dto.UserResponseDTO;
import io.github.ezequiellima10.fintech.user.model.User;
import org.springframework.data.domain.Pageable;


public interface UserService {

    UserResponseDTO create(UserRequestDTO dto);

    ResponsePageDTO<UserResponseDTO> findAll(Pageable page);

    UserResponseDTO findByIdDto(Long id);

    User findById(Long id);
}
