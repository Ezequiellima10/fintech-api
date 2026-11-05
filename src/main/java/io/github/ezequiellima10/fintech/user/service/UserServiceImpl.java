package io.github.ezequiellima10.fintech.user.service;

import io.github.ezequiellima10.fintech.shared.dto.ResponsePageDTO;
import io.github.ezequiellima10.fintech.user.dto.UserRequestDTO;
import io.github.ezequiellima10.fintech.user.dto.UserResponseDTO;
import io.github.ezequiellima10.fintech.user.exception.UserAlreadyExistsException;
import io.github.ezequiellima10.fintech.user.exception.UserNotFoundException;
import io.github.ezequiellima10.fintech.user.mapper.UserMapper;
import io.github.ezequiellima10.fintech.user.model.User;
import io.github.ezequiellima10.fintech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    @Transactional
    public UserResponseDTO create(UserRequestDTO dto) {

        if (userRepository.existsByEmail(dto.email())) {
            throw new UserAlreadyExistsException("Ya existe un usuario con el email " + dto.email());
        }

        if (userRepository.existsByUsername(dto.username())) {
            throw new UserAlreadyExistsException("Ya existe un usuario con el nombre de usuario " + dto.username());
        }

        return userMapper.entityToResponseDTO(userRepository.save(userMapper.requestToEntity(dto)));
    }

    @Override
    public ResponsePageDTO<UserResponseDTO> findAll(Pageable page) {
        Page<User> userPage = userRepository.findAll(page);
        Page<UserResponseDTO> dtos = userPage.map(userMapper::entityToResponseDTO);
        return new ResponsePageDTO<>(dtos.getContent(), dtos.getNumber(), dtos.getSize(), dtos.getTotalElements(), dtos.getTotalPages());
    }

    @Override
    public UserResponseDTO findByIdDto(Long id) {
        return userMapper.entityToResponseDTO(this.findById(id));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("no existe un usuario con el id: " + id));
    }
}
