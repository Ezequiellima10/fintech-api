package io.github.ezequiellima10.fintech.account.exception;

public class NonZeroBalanceException extends RuntimeException {
    public NonZeroBalanceException(String message) {
        super(message);
    }
}
