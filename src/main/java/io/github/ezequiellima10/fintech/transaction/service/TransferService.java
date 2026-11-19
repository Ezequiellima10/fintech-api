package io.github.ezequiellima10.fintech.transaction.service;

import io.github.ezequiellima10.fintech.transaction.dto.TransactionResponseDTO;
import io.github.ezequiellima10.fintech.transaction.dto.TransferRequestDTO;
import io.github.ezequiellima10.fintech.transaction.dto.TransferResponseDTO;

import java.util.List;

public interface TransferService {

    TransferResponseDTO transfer(TransferRequestDTO transferRequestDTO);

    List<TransferResponseDTO> movements(Long AccountId);
}
