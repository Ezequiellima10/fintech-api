package io.github.ezequiellima10.fintech.account.repository;

import io.github.ezequiellima10.fintech.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {

    boolean existsByAlias(String alias);

}
