package io.github.ezequiellima10.fintech.user.service;

import io.github.ezequiellima10.fintech.shared.dto.ResponsePageDTO;
import io.github.ezequiellima10.fintech.user.dto.UserRequestDTO;
import io.github.ezequiellima10.fintech.user.dto.UserResponseDTO;
import io.github.ezequiellima10.fintech.user.exception.UserAlreadyExistsException;
import io.github.ezequiellima10.fintech.user.exception.UserNotFoundException;
import io.github.ezequiellima10.fintech.user.mapper.UserMapper;
import io.github.ezequiellima10.fintech.user.model.User;
import io.github.ezequiellima10.fintech.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void create_validData_savesAndReturnsDto() {
        // given
        UserRequestDTO request = new UserRequestDTO("juan", "juan@mail.com");
        User newUser = User.builder().username("juan").email("juan@mail.com").build();
        User savedUser = User.builder().id(1L).username("juan").email("juan@mail.com").build();
        UserResponseDTO expectedResponse = UserResponseDTO.builder().id(1L).username("juan").email("juan@mail.com").build();

        when(userRepository.existsByEmail("juan@mail.com")).thenReturn(false);
        when(userRepository.existsByUsername("juan")).thenReturn(false);
        when(userMapper.requestToEntity(request)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(savedUser);
        when(userMapper.entityToResponseDTO(savedUser)).thenReturn(expectedResponse);

        // when
        UserResponseDTO result = userService.create(request);

        // then
        assertEquals(expectedResponse, result);
        verify(userRepository).save(newUser);
    }

    @Test
    void create_duplicateEmail_throwsUserAlreadyExistsException() {
        // given
        UserRequestDTO request = new UserRequestDTO("juan", "juan@mail.com");

        when(userRepository.existsByEmail("juan@mail.com")).thenReturn(true);

        // when / then
        assertThrows(UserAlreadyExistsException.class, () -> userService.create(request));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void create_duplicateUsername_throwsUserAlreadyExistsException() {
        // given
        UserRequestDTO request = new UserRequestDTO("juan", "juan@mail.com");

        when(userRepository.existsByEmail("juan@mail.com")).thenReturn(false);
        when(userRepository.existsByUsername("juan")).thenReturn(true);

        // when / then
        assertThrows(UserAlreadyExistsException.class, () -> userService.create(request));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findById_nonexistent_throwsUserNotFoundException() {
        // given
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // when / then
        assertThrows(UserNotFoundException.class, () -> userService.findById(99L));
    }

    @Test
    void findByIdDto_existing_returnsDto() {
        // given
        User user = User.builder().id(1L).username("juan").email("juan@mail.com").build();
        UserResponseDTO expectedResponse = UserResponseDTO.builder().id(1L).username("juan").email("juan@mail.com").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.entityToResponseDTO(user)).thenReturn(expectedResponse);

        // when
        UserResponseDTO result = userService.findByIdDto(1L);

        // then
        assertEquals(expectedResponse, result);
    }

    @Test
    void findAll_mapsPageToDtos() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        User juan = User.builder().id(1L).username("juan").email("juan@mail.com").build();
        User ana = User.builder().id(2L).username("ana").email("ana@mail.com").build();
        Page<User> page = new PageImpl<>(List.of(juan, ana), pageable, 2);

        UserResponseDTO juanDto = UserResponseDTO.builder().id(1L).username("juan").build();
        UserResponseDTO anaDto = UserResponseDTO.builder().id(2L).username("ana").build();

        when(userRepository.findAll(pageable)).thenReturn(page);
        when(userMapper.entityToResponseDTO(juan)).thenReturn(juanDto);
        when(userMapper.entityToResponseDTO(ana)).thenReturn(anaDto);

        // when
        ResponsePageDTO<UserResponseDTO> result = userService.findAll(pageable);

        // then
        assertEquals(List.of(juanDto, anaDto), result.dtos());
        assertEquals(2, result.totalElements());
        assertEquals(1, result.totalPages());
    }
}
