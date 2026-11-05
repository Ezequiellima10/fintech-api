package io.github.ezequiellima10.fintech.account.service;

import io.github.ezequiellima10.fintech.account.dto.AccountRequestDTO;
import io.github.ezequiellima10.fintech.account.dto.AccountRequestUpdateDTO;
import io.github.ezequiellima10.fintech.account.dto.AccountResponseDTO;
import io.github.ezequiellima10.fintech.account.exception.AccountAlreadyExistsException;
import io.github.ezequiellima10.fintech.account.exception.AccountNotFoundException;
import io.github.ezequiellima10.fintech.account.mapper.AccountMapper;
import io.github.ezequiellima10.fintech.account.model.Account;
import io.github.ezequiellima10.fintech.account.model.AccountType;
import io.github.ezequiellima10.fintech.account.model.CheckingAccount;
import io.github.ezequiellima10.fintech.account.model.SavingsAccount;
import io.github.ezequiellima10.fintech.account.repository.AccountRepository;
import io.github.ezequiellima10.fintech.shared.dto.ResponsePageDTO;
import io.github.ezequiellima10.fintech.user.model.User;
import io.github.ezequiellima10.fintech.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private AccountServiceImpl accountService;

    private final User holder = User.builder().id(1L).username("juan").email("juan@mail.com").build();


    @Test
    void create_savingsAccount_savesSavingsAccountWithHolder() {
        // given
        AccountRequestDTO request = new AccountRequestDTO(1L, "mi.caja", AccountType.SAVINGS, null);
        SavingsAccount savedAccount = new SavingsAccount(holder, "mi.caja");
        AccountResponseDTO expectedResponse = AccountResponseDTO.builder().alias("mi.caja").accountType(AccountType.SAVINGS).build();

        when(accountRepository.existsByAlias("mi.caja")).thenReturn(false);
        when(userService.findById(1L)).thenReturn(holder);
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);
        when(accountMapper.entityToResponseDTO(savedAccount)).thenReturn(expectedResponse);

        // when
        AccountResponseDTO result = accountService.create(request);

        // then
        assertEquals(expectedResponse, result);

        // the captor "grabs" the account the service passed to save() so it can be inspected
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());
        Account accountToSave = captor.getValue();

        assertInstanceOf(SavingsAccount.class, accountToSave);
        assertEquals("mi.caja", accountToSave.getAlias());
        assertEquals(holder, accountToSave.getHolder());
    }

    @Test
    void create_checkingAccount_savesWithOverdraftLimit() {
        // given
        AccountRequestDTO request = new AccountRequestDTO(1L, "mi.corriente", AccountType.CHECKING, new BigDecimal("5000"));
        CheckingAccount savedAccount = new CheckingAccount(holder, "mi.corriente", new BigDecimal("5000"));
        AccountResponseDTO expectedResponse = AccountResponseDTO.builder().alias("mi.corriente").accountType(AccountType.CHECKING).build();

        when(accountRepository.existsByAlias("mi.corriente")).thenReturn(false);
        when(userService.findById(1L)).thenReturn(holder);
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);
        when(accountMapper.entityToResponseDTO(savedAccount)).thenReturn(expectedResponse);

        // when
        AccountResponseDTO result = accountService.create(request);

        // then
        assertEquals(expectedResponse, result);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());
        Account accountToSave = captor.getValue();

        assertInstanceOf(CheckingAccount.class, accountToSave);
        assertEquals(new BigDecimal("5000"), ((CheckingAccount) accountToSave).getOverdraftLimit());
    }

    @Test
    void create_duplicateAlias_throwsAccountAlreadyExistsException() {
        // given
        AccountRequestDTO request = new AccountRequestDTO(1L, "mi.caja", AccountType.SAVINGS, null);

        when(accountRepository.existsByAlias("mi.caja")).thenReturn(true);

        // when / then
        assertThrows(AccountAlreadyExistsException.class, () -> accountService.create(request));

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void update_newAlias_updatesAlias() {
        // given
        Account account = new SavingsAccount(holder, "alias.viejo");
        AccountRequestUpdateDTO request = new AccountRequestUpdateDTO("alias.nuevo");
        AccountResponseDTO expectedResponse = AccountResponseDTO.builder().alias("alias.nuevo").build();

        when(accountRepository.findById(10L)).thenReturn(Optional.of(account));
        when(accountRepository.existsByAlias("alias.nuevo")).thenReturn(false);
        when(accountMapper.entityToResponseDTO(account)).thenReturn(expectedResponse);

        // when
        AccountResponseDTO result = accountService.update(10L, request);

        // then
        assertEquals("alias.nuevo", account.getAlias());
        assertEquals(expectedResponse, result);
    }

    @Test
    void update_aliasFromAnotherAccount_throwsAccountAlreadyExistsException() {
        // given
        Account account = new SavingsAccount(holder, "alias.viejo");
        AccountRequestUpdateDTO request = new AccountRequestUpdateDTO("alias.ocupado");

        when(accountRepository.findById(10L)).thenReturn(Optional.of(account));
        when(accountRepository.existsByAlias("alias.ocupado")).thenReturn(true);

        // when / then
        assertThrows(AccountAlreadyExistsException.class, () -> accountService.update(10L, request));

        assertEquals("alias.viejo", account.getAlias());
    }

    @Test
    void update_nonexistentAccount_throwsAccountNotFoundException() {
        // given
        AccountRequestUpdateDTO request = new AccountRequestUpdateDTO("alias.nuevo");

        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        // when / then
        assertThrows(AccountNotFoundException.class, () -> accountService.update(99L, request));
    }

    @Test
    void findById_nonexistent_throwsAccountNotFoundException() {
        // given
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        // when / then
        assertThrows(AccountNotFoundException.class, () -> accountService.findById(99L));
    }

    @Test
    void findAll_mapsPageToDtos() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Account savings = new SavingsAccount(holder, "caja.uno");
        Account checking = new CheckingAccount(holder, "corriente.uno", new BigDecimal("1000"));
        Page<Account> page = new PageImpl<>(List.of(savings, checking), pageable, 2);

        AccountResponseDTO savingsDto = AccountResponseDTO.builder().alias("caja.uno").build();
        AccountResponseDTO checkingDto = AccountResponseDTO.builder().alias("corriente.uno").build();

        when(accountRepository.findAll(pageable)).thenReturn(page);
        when(accountMapper.entityToResponseDTO(savings)).thenReturn(savingsDto);
        when(accountMapper.entityToResponseDTO(checking)).thenReturn(checkingDto);

        // when
        ResponsePageDTO<AccountResponseDTO> result = accountService.findAll(pageable);

        // then
        assertEquals(List.of(savingsDto, checkingDto), result.dtos());
        assertEquals(0, result.pageNumber());
        assertEquals(10, result.pageSize());
        assertEquals(2, result.totalElements());
        assertEquals(1, result.totalPages());
    }

    @Test
    void delete_existing_deletesAccount() {
        // given
        when(accountRepository.existsById(10L)).thenReturn(true);

        // when
        accountService.delete(10L);

        // then
        verify(accountRepository).deleteById(10L);
    }

    @Test
    void delete_nonexistent_throwsExceptionAndDoesNotDelete() {
        // given
        when(accountRepository.existsById(99L)).thenReturn(false);

        // when / then
        assertThrows(AccountNotFoundException.class, () -> accountService.delete(99L));

        verify(accountRepository, never()).deleteById(any());
    }
}
