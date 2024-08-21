package com.boii.bankapp.controller;


import com.boii.bankapp.dto.*;
import com.boii.bankapp.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }

    @GetMapping("balanceEnquiry")
    public BankResponse getBalanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.balanceEnquiry(enquiryRequest);
    }

    @GetMapping("nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.nameEnquiry(enquiryRequest);
    }
    @PostMapping("credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest creditRequest) {
        return userService.creditAccount(creditRequest);
    }
    @PostMapping("debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest debitRequest){
        return userService.debitAccount(debitRequest);
    }
    @PostMapping("transfer")
    public BankResponse transfer(@RequestBody TransferRequest transferRequest){
        return userService.transfer(transferRequest);
    }
}
