package io.github.ezequiellima10.fintech.account.model;

import io.github.ezequiellima10.fintech.account.exception.InsufficientFundsException;
import io.github.ezequiellima10.fintech.account.exception.InvalidAmountException;
import io.github.ezequiellima10.fintech.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "holder_id", nullable = false)
    private User holder;

    @Setter
    @NotBlank
    @Column(nullable = false, unique = true)
    private String alias;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    protected Account(User holder, String alias) {
        this.holder = holder;
        this.alias = alias;
    }

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void deposit(BigDecimal amount) {
        validateAmount(amount);
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        validateAmount(amount);
        if (!canWithdraw(amount)) {
            throw new InsufficientFundsException("Saldo insuficiente");
        }
        balance = balance.subtract(amount);
    }

    protected abstract boolean canWithdraw(BigDecimal amount);

    public abstract AccountType getAccountType();

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new InvalidAmountException("El monto debe ser mayor a cero");
        }
    }
}
