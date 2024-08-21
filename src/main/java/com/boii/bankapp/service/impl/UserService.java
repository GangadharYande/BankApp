package com.boii.bankapp.service.impl;

import com.boii.bankapp.dto.*;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);

    // credit and debit
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);


    BankResponse  transfer(TransferRequest request);

}
