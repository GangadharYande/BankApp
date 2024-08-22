package com.boii.bankapp.controller;


import com.boii.bankapp.dto.*;
import com.boii.bankapp.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name ="User Account Management APIs")
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(
            summary = "Create New User Account",
            description = "Creating a new user and assigning an account ID"
    )
    @ApiResponse(
        responseCode ="201",
            description =" Http Status 201 CREATED"
    )
    @PostMapping("/register")
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }
    @Operation(
            summary = "Balance Enquiry",
            description = "Given an accountNumber,display available balance "
    )
    @ApiResponse(
            responseCode ="200",
            description =" Http Status 201 SUCCESS"
    )

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
