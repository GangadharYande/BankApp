package com.boii.bankapp.service.impl;

import com.boii.bankapp.dto.TransactionDTO;


public interface TransactionService {
    void saveTransaction(TransactionDTO transactionDTO);
}
