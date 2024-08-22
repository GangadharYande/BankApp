package com.boii.bankapp.service.impl;

import com.boii.bankapp.dto.TransactionDTO;
import com.boii.bankapp.entity.Transaction;
import com.boii.bankapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDTO transactionDTO){
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDTO.getTransactionType())
                .accountNumber(transactionDTO.getAccountNumber())
                .amount(transactionDTO.getAmount())
                .status("SUCCESS")
                .build();
        transactionRepository.save(transaction);
        System.out.println("Transaction Saved Successfully");
    }
}
