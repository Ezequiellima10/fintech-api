package io.github.ezequiellima10.fintech.account.model;

import io.github.ezequiellima10.fintech.user.model.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("SAVINGS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavingsAccount extends Account {

    public SavingsAccount(User holder, String alias) {
        super(holder, alias);
    }

    @Override
    protected boolean canWithdraw(BigDecimal amount) {

        return getBalance().compareTo(amount) >= 0;
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.SAVINGS;}
}
