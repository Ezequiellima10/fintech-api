package io.github.ezequiellima10.fintech.account.service;

import io.github.ezequiellima10.fintech.account.dto.AccountRequestDTO;
import io.github.ezequiellima10.fintech.account.dto.AccountRequestUpdateDTO;
import io.github.ezequiellima10.fintech.account.dto.AccountResponseDTO;
import io.github.ezequiellima10.fintech.shared.dto.ResponsePageDTO;
import org.springframework.data.domain.Pageable;

public interface AccountService {

    AccountResponseDTO create(AccountRequestDTO accountRequestDTO);

    ResponsePageDTO<AccountResponseDTO> findAll(Pageable pageable);

    AccountResponseDTO findById(Long id);

    AccountResponseDTO update(Long id, AccountRequestUpdateDTO accountRequestUpdateDTO);

    void delete(Long id);
}
