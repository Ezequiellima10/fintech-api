package io.github.ezequiellima10.fintech.account.mapper;

import io.github.ezequiellima10.fintech.account.dto.AccountResponseDTO;
import io.github.ezequiellima10.fintech.account.model.Account;
import io.github.ezequiellima10.fintech.account.model.CheckingAccount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountMapper {

    public AccountResponseDTO entityToResponseDTO(Account account) {
        BigDecimal overdraftLimit = account instanceof CheckingAccount ? ((CheckingAccount) account).getOverdraftLimit() : BigDecimal.ZERO;
        return AccountResponseDTO.builder().id(account.getId())
                                            .alias(account.getAlias())
                                            .accountType(account.getAccountType())
                                            .balance(account.getBalance())
                                            .overdraftLimit(overdraftLimit)
                                            .holderId(account.getHolder().getId())
                                            .createdAt(account.getCreatedAt())
                                            .build();
    }
}
