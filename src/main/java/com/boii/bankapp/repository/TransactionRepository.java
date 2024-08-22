package com.boii.bankapp.repository;

import com.boii.bankapp.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,String> {

}
