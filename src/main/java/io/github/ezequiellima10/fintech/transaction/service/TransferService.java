package io.github.ezequiellima10.fintech.transaction.service;

import io.github.ezequiellima10.fintech.transaction.dto.TransferRequestDTO;
import io.github.ezequiellima10.fintech.transaction.dto.TransferResponseDTO;


public interface TransferService {

    TransferResponseDTO transfer(TransferRequestDTO transferRequestDTO);
}
