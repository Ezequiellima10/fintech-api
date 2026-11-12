package io.github.ezequiellima10.fintech.transaction.repository;

import io.github.ezequiellima10.fintech.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
