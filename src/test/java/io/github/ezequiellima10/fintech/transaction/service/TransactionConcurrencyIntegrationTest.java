package io.github.ezequiellima10.fintech.transaction.service;

import io.github.ezequiellima10.fintech.account.dto.AccountRequestDTO;
import io.github.ezequiellima10.fintech.account.dto.AccountResponseDTO;
import io.github.ezequiellima10.fintech.account.model.AccountType;
import io.github.ezequiellima10.fintech.account.service.AccountService;
import io.github.ezequiellima10.fintech.transaction.dto.TransactionRequestDTO;
import io.github.ezequiellima10.fintech.user.dto.UserRequestDTO;
import io.github.ezequiellima10.fintech.user.dto.UserResponseDTO;
import io.github.ezequiellima10.fintech.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@Testcontainers
class TransactionConcurrencyIntegrationTest {

    @Container
    @ServiceConnection
    static MySQLContainer mysql = new MySQLContainer("mysql:8.4");

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    private Long accountId;

    @BeforeEach
    public void createAccount() {

        UserRequestDTO userRequestDTO = new UserRequestDTO("UsuarioConcurrencia", "UsuarioConcurrencia@test.com");
        UserResponseDTO userResponseDTO = userService.create(userRequestDTO);

        AccountRequestDTO accountRequestDTO = new AccountRequestDTO(userResponseDTO.id(), "cuenta.concurrencia", AccountType.SAVINGS, null);
        AccountResponseDTO accountResponseDTO = accountService.create(accountRequestDTO);

        TransactionRequestDTO depositRequestDTO = new TransactionRequestDTO(new BigDecimal("1000.00"), "Saldo inicial");
        transactionService.deposit(depositRequestDTO, accountResponseDTO.id());

        this.accountId = accountResponseDTO.id();
    }

    @Test
    public void withdraw_concurrentRequests_noLostUpdate() throws Exception {
        //Given
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(1);

        TransactionRequestDTO withdrawRequestDTO = new TransactionRequestDTO(new BigDecimal("100.00"), "Extraccion concurrente");
        List<Future<?>> futures = new ArrayList<>();

        //When
        for (int i = 0; i < 10; i++) {
            futures.add(executorService.submit(() -> {
                countDownLatch.await();
                return transactionService.withdraw(withdrawRequestDTO, accountId);
            }));
        }
        countDownLatch.countDown();

        for (Future<?> future : futures) {
            future.get();
        }
        executorService.shutdown();

        //Then
        AccountResponseDTO accountResultDto = accountService.findById(accountId);

        System.out.println("----------------------Saldo final: " + accountResultDto.balance() + "-------------------------");
        assertThat(accountResultDto.balance()).isEqualByComparingTo("0.00");
    }


}