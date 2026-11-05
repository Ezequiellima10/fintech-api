package io.github.ezequiellima10.fintech.account.service;

import io.github.ezequiellima10.fintech.account.dto.AccountRequestDTO;
import io.github.ezequiellima10.fintech.account.dto.AccountRequestUpdateDTO;
import io.github.ezequiellima10.fintech.account.dto.AccountResponseDTO;
import io.github.ezequiellima10.fintech.account.exception.AccountAlreadyExistsException;
import io.github.ezequiellima10.fintech.account.exception.AccountNotFoundException;
import io.github.ezequiellima10.fintech.account.mapper.AccountMapper;
import io.github.ezequiellima10.fintech.account.model.Account;
import io.github.ezequiellima10.fintech.account.model.CheckingAccount;
import io.github.ezequiellima10.fintech.account.model.SavingsAccount;
import io.github.ezequiellima10.fintech.account.repository.AccountRepository;
import io.github.ezequiellima10.fintech.shared.dto.ResponsePageDTO;
import io.github.ezequiellima10.fintech.user.model.User;
import io.github.ezequiellima10.fintech.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserService userService;


    @Override
    @Transactional
    public AccountResponseDTO create(AccountRequestDTO accountRequestDTO) {

        if (accountRepository.existsByAlias(accountRequestDTO.alias())){
            throw new AccountAlreadyExistsException("Ya existe una cuenta con el alias " + accountRequestDTO.alias());
        }

        User holder = userService.findById(accountRequestDTO.holderId());

        Account account = switch (accountRequestDTO.accountType()) {
            case SAVINGS -> new SavingsAccount(holder, accountRequestDTO.alias());
            case CHECKING -> new CheckingAccount(holder, accountRequestDTO.alias(), accountRequestDTO.overdraftLimit());
        };


        return accountMapper.entityToResponseDTO(accountRepository.save(account));
    }

    @Override
    public ResponsePageDTO<AccountResponseDTO> findAll(Pageable page) {
        Page<Account> accountPage = accountRepository.findAll(page);
        Page<AccountResponseDTO> dtoPage = accountPage.map(accountMapper::entityToResponseDTO);
        return new ResponsePageDTO<>(dtoPage.getContent(), dtoPage.getNumber(), dtoPage.getSize(), dtoPage.getTotalElements(), dtoPage.getTotalPages());
    }

    @Override
    public AccountResponseDTO findById(Long id) {
        return accountMapper.entityToResponseDTO((accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("No existe la cuenta con el id: " + id))));
    }


    @Override
    @Transactional
    public AccountResponseDTO update(Long id, AccountRequestUpdateDTO dto) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("no existe una cuenta con el id: " + id));

        if (!dto.alias().equals(account.getAlias()) && accountRepository.existsByAlias(dto.alias())) {
            throw new AccountAlreadyExistsException("Ya existe una cuenta con el alias " + dto.alias());
        }

        account.setAlias(dto.alias());

        return accountMapper.entityToResponseDTO(account);
    }


    @Override
    @Transactional
    public void delete(Long id) {

        if (!accountRepository.existsById(id)) {
            throw new AccountNotFoundException("No existe la cuenta con el id: " + id);
        }
        accountRepository.deleteById(id);
    }
}
